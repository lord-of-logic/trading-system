package com.ranjith.orderTypeProcessor

import com.ranjith.entities.Order
import com.ranjith.entities.Trade
import com.ranjith.enums.OrderType
import com.ranjith.enums.TradeType
import com.ranjith.repository.OrderRepository
import com.ranjith.repository.TradeRepository
import com.ranjith.service.StockService
import com.ranjith.service.TradeService
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class SellOrderTypeProcessor(
    private val stockService: StockService,
    private val orderRepository: OrderRepository,
    private val tradeRepository: TradeRepository,
    private val tradeService: TradeService,
) : OrderTypeProcessor {
    companion object {
        private val log = LoggerFactory.getLogger(SellOrderTypeProcessor::class.java)
    }

    @Transactional
    override fun executeTradeByOrder(order: Order) {
        val stockId = order.stock?.stockId ?: throw RuntimeException("StockId not found for OrderId: ${order.orderId}")
        val stock = stockService.getByStockId(stockId) ?: throw RuntimeException("Stock not found for StockId: ${order.stock?.stockId}")

        val ordersToUpdateAsCompleted = mutableListOf<Order>()
        val executedTrades = mutableListOf<Trade>()

        val eligibleBuyOrders = orderRepository.getAcceptedOrdersByStockIdAndOrderTypeFromLastThirtyMinutes(stockId, OrderType.BUY)
            .filter { it.price!! >= order.price!! }
            .sortedWith(compareByDescending<Order> { it.price }.thenBy { it.orderAcceptedAt }).toMutableList()
        if (eligibleBuyOrders.isEmpty()) {
            log.info("No eligible Buy orders found for stockId: $stockId")
            return
        }
        var eligibleBuyOrderQuantity = eligibleBuyOrders.sumOf { it.remainingQuantity!! }

        val jobs = mutableListOf<Job>()
        val scope = CoroutineScope(Dispatchers.Default)
        val mutex = Mutex()

        while (order.remainingQuantity!! > 0 && eligibleBuyOrderQuantity > 0 && eligibleBuyOrders.isNotEmpty()) {
            val buyOrder = eligibleBuyOrders.removeFirst()
            val tradeQuantity = minOf(order.remainingQuantity!!, buyOrder.remainingQuantity!!)

            val job = scope.launch {
                val executedTrade = tradeService.executeTrade(
                    stock,
                    buyOrder,
                    order,
                    TradeType.SELL,
                    tradeQuantity,
                    ordersToUpdateAsCompleted
                )
                mutex.withLock {
                    if (executedTrade != null) {
                        executedTrades.add(executedTrade)
                        eligibleBuyOrderQuantity -= tradeQuantity
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
