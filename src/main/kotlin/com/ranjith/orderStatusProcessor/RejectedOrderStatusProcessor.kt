package com.ranjith.orderStatusProcessor

import com.ranjith.dto.OrderDTO
import com.ranjith.entities.Order
import org.slf4j.LoggerFactory

class RejectedOrderStatusProcessor: OrderStatusProcessor {
    companion object {
        private val log = LoggerFactory.getLogger(RejectedOrderStatusProcessor::class.java)
    }

    override fun cancelOrder(order: Order): Order {
        log.info("Order with order id: ${order.orderId} is already in REJECTED state")
        throw RuntimeException("Order with order id: ${order.orderId} is already in REJECTED state")
    }

    override fun modifyOrder(currOrder: Order, newOrderDTO: OrderDTO): Order {
        log.info("Order with order id: ${currOrder.orderId} is in REJECTED state. Modification not allowed")
        throw RuntimeException("Order with order id: ${currOrder.orderId} is in REJECTED state. Modification not allowed")
    }
}
