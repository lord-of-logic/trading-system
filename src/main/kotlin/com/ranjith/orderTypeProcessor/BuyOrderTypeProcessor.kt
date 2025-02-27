package com.ranjith.orderTypeProcessor

import com.ranjith.entities.Order
import com.ranjith.entities.Trade
import com.ranjith.enums.OrderType
import com.ranjith.enums.TradeType
import com.ranjith.repository.OrderRepository
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
    private val stockService: StockService,
    private val orderRepository: OrderRepository,
    private val tradeRepository: TradeRepository,
    private val tradeService: TradeService
) : OrderTypeProcessor {
    companion object {
        private val log = LoggerFactory.getLogger(BuyOrderTypeProcessor::class.java)
    }

    @Transactional
    override fun executeTradeByOrder(order: Order) {
        val stockId = order.stock?.stockId ?: throw RuntimeException("StockId not found for OrderId: ${order.orderId}")
        val stock = stockService.getByStockId(stockId) ?: throw RuntimeException("Stock not found for StockId: ${order.stock?.stockId}")

        val ordersToUpdateAsCompleted = mutableListOf<Order>()
        val executedTrades = mutableListOf<Trade>()

        val eligibleSellOrders = orderRepository.getAcceptedOrdersByStockIdAndOrderTypeFromLastThirtyMinutes(stockId, OrderType.SELL)
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
                    if (executedTrade != null) {
                        executedTrades.add(executedTrade)
                        eligibleSellOrderQuantity -= tradeQuantity
                    }
                }
            }
            jobs.add(job)
        }
        runBlocking {
            jobs.forEach { it.join() }
        }

        orderRepository.saveAll(ordersToUpdateAsCompleted)
        val savedTrades = tradeRepository.saveAll(executedTrades)
        stockService.updateStockPriceByTrades(stock, savedTrades)
    }
}
