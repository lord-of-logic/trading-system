package com.ranjith.orderTypeProcessor

import com.ranjith.enums.OrderType
import com.ranjith.repository.OrderRepository
import com.ranjith.repository.StockRepository
import com.ranjith.repository.TradeRepository
import com.ranjith.service.StockService
import com.ranjith.service.TradeService
import org.springframework.stereotype.Component

@Component
class OrderTypeProcessorFactory(
    private val stockRepository: StockRepository,
    private val orderRepository: OrderRepository,
    private val tradeRepository: TradeRepository,
    private val tradeService: TradeService,
    private val stockService: StockService
){
    fun getOrderTypeProcessor(orderType: OrderType): OrderTypeProcessor {
        return when (orderType) {
            OrderType.BUY -> BuyOrderTypeProcessor(stockRepository, orderRepository, tradeRepository, tradeService, stockService)
            OrderType.SELL -> SellOrderTypeProcessor(stockRepository, orderRepository, tradeRepository, tradeService, stockService)
            else -> throw RuntimeException("Invalid order type")
        }
    }
}
