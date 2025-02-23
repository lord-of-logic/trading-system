package com.ranjith.orderTypeProcessor

import com.ranjith.entities.Order
import com.ranjith.entities.Trade
import com.ranjith.enums.OrderType
import com.ranjith.enums.TradeType
import com.ranjith.repository.OrderRepository
import com.ranjith.repository.StockRepository
import com.ranjith.repository.TradeRepository
import com.ranjith.service.StockService
import com.ranjith.service.TradeService
import org.slf4j.LoggerFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class BuyOrderTypeProcessor(
    private val stockRepository: StockRepository,
    private val orderRepository: OrderRepository,
    private val tradeRepository: TradeRepository,
    private val tradeService: TradeService,
    private val stockService: StockService
) : OrderTypeProcessor {
    companion object {
        private val log = LoggerFactory.getLogger(BuyOrderTypeProcessor::class.java)
    }

    @Transactional
    override fun executeTradeByStockId(stockId: Long, order: Order) {
        log.info("Executing Buy Order for StockId: $stockId")
        val stock =
            stockRepository.getByStockId(stockId) ?: throw RuntimeException("Stock not found for StockId: $stockId")

        val ordersToUpdateAsCompleted = mutableListOf<Order>()
        val executedTrades = mutableListOf<Trade>()

        val eligibleSellOrders = orderRepository.getAcceptedOrdersByStockIdAndOrderType(stockId, OrderType.SELL)
            .filter { it.price!! <= order.price!! }
            .sortedWith(compareBy<Order> { it.price }.thenBy { it.createdOn }).toMutableList()
        if (eligibleSellOrders.isEmpty()) {
            log.info("No eligible buy orders found for stockId: $stockId")
            return
        }
        var eligibleSellOrderQuantity = eligibleSellOrders.sumOf { it.remainingQuantity!! }

        val scope = CoroutineScope(Dispatchers.Default)
        val jobs = mutableListOf<Job>()
        val mutex = Mutex()

        while (order.remainingQuantity!! > 0 && eligibleSellOrderQuantity > 0 && eligibleSellOrders.isNotEmpty()) {
            val sellOrder = eligibleSellOrders.removeFirst()
            val tradeQuantity = minOf(order.remainingQuantity!!, sellOrder.remainingQuantity!!)

            val job = scope.launch {
                val executedTrade = tradeService.executeTrade(
                    stock,
                    order,
                    sellOrder,
                    TradeType.BUY,
                    tradeQuantity,
                    ordersToUpdateAsCompleted
                )
                mutex.withLock {
                    executedTrades.add(executedTrade)
                }
            }
            jobs.add(job)
            eligibleSellOrderQuantity -= tradeQuantity
        }
        runBlocking {
            jobs.forEach { it.join() }
        }

        orderRepository.saveAll(ordersToUpdateAsCompleted)
        val savedTrades = tradeRepository.saveAll(executedTrades)
        stockService.updateStockPriceByTrades(stock, savedTrades)
    }
}
