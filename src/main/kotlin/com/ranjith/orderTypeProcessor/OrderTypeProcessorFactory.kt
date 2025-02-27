package com.ranjith.orderTypeProcessor

import com.ranjith.enums.OrderType
import org.springframework.stereotype.Component

@Component
class OrderTypeProcessorFactory(
    private val buyOrderTypeProcessor: BuyOrderTypeProcessor,
    private val sellOrderTypeProcessor: SellOrderTypeProcessor
){
    fun getOrderTypeProcessor(orderType: OrderType): OrderTypeProcessor {
        return when (orderType) {
            OrderType.BUY -> buyOrderTypeProcessor
            OrderType.SELL -> sellOrderTypeProcessor
            else -> throw RuntimeException("Invalid order type")
        }
    }
}
