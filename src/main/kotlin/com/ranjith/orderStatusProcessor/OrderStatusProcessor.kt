package com.ranjith.orderStatusProcessor

import com.ranjith.dto.OrderDTO
import com.ranjith.entities.Order
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
interface OrderStatusProcessor {
    companion object {
        private val log = LoggerFactory.getLogger(OrderStatusProcessor::class.java)
    }

    fun cancelOrder(order: Order): Order {
        throw RuntimeException("Order cancellation not allowed")
    }

    fun modifyOrder(currOrder: Order, newOrderDTO: OrderDTO): Order {
        throw RuntimeException("Order modification not allowed")
    }
}
