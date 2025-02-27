package com.ranjith.orderTypeProcessor

import com.ranjith.enums.OrderType
import com.ranjith.repository.OrderRepository
import com.ranjith.repository.StockRepository
import com.ranjith.repository.TradeRepository
import com.ranjith.service.OrderBookService
import com.ranjith.service.StockService
import com.ranjith.service.TradeService
import org.springframework.stereotype.Component

@Component
class OrderTypeProcessorFactory(
    private val stockService: StockService,
    private val orderRepository: OrderRepository,
    private val tradeRepository: TradeRepository,
    private val tradeService: TradeService,
){
    fun getOrderTypeProcessor(orderType: OrderType): OrderTypeProcessor {
        return when (orderType) {
            OrderType.BUY -> BuyOrderTypeProcessor(stockService, orderRepository, tradeRepository, tradeService)
            OrderType.SELL -> SellOrderTypeProcessor(stockService, orderRepository, tradeRepository, tradeService)
            else -> throw RuntimeException("Invalid order type")
        }
    }
}
