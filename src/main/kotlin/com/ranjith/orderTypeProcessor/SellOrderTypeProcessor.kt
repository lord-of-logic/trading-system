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
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class SellOrderTypeProcessor(
    private val stockRepository: StockRepository,
    private val orderRepository: OrderRepository,
    private val tradeRepository: TradeRepository,
    private val tradeService: TradeService,
    private val stockService: StockService
): OrderTypeProcessor {
    companion object {
        private val log = LoggerFactory.getLogger(SellOrderTypeProcessor::class.java)
    }

    @Transactional
    override fun executeTradeByStockId(stockId: Long, order: Order) {
        log.info("Executing Sell Order for StockId: $stockId")
        val stock = stockRepository.getByStockId(stockId) ?: throw RuntimeException("Stock not found for StockId: $stockId")

        val ordersToUpdateAsCompleted = mutableListOf<Order>()
        val executedTrades = mutableListOf<Trade>()

        val eligibleBuyOrders = orderRepository.getAcceptedOrdersByStockIdAndOrderType(stockId, OrderType.BUY)
            .filter { it.price!! >= order.price!! }
            .sortedWith(compareByDescending<Order> { it.price }.thenBy { it.createdOn }).toMutableList()
        if(eligibleBuyOrders.isEmpty()) {
            log.info("No eligible Buy orders found for stockId: $stockId")
            return
        }
        var eligibleBuyOrderQuantity = eligibleBuyOrders.sumOf { it.remainingQuantity!! }

        //ToDo: Add concurrency to execute trades in parallel for each buy order
        while (order.remainingQuantity!! > 0 && eligibleBuyOrderQuantity > 0 && eligibleBuyOrders.isNotEmpty()) {
            val buyOrder = eligibleBuyOrders.removeFirst()
            val executedTrade = tradeService.executeTrade(stock, buyOrder, order, TradeType.SELL, ordersToUpdateAsCompleted)
            eligibleBuyOrderQuantity -= executedTrade.quantity!!
            executedTrades.add(executedTrade)
        }

        orderRepository.saveAll(ordersToUpdateAsCompleted)
        val savedTrades = tradeRepository.saveAll(executedTrades)
        stockService.updateStockPriceByTrades(stock, savedTrades)
    }
}
