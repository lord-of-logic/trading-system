package com.ranjith.orderStatusProcessor

import com.ranjith.dto.OrderDTO
import com.ranjith.entities.Order
import org.slf4j.LoggerFactory

class CanceledOrderStatusProcessor: OrderStatusProcessor {
    companion object {
        private val log = LoggerFactory.getLogger(CanceledOrderStatusProcessor::class.java)
    }

    override fun cancelOrder(order: Order): Order {
        log.info("Order with order id: ${order.orderId} is already in CANCELED state")
        return order
    }

    override fun modifyOrder(currOrder: Order, newOrderDTO: OrderDTO): Order {
        log.info("Order with order id: ${currOrder.orderId} is in CANCELED state. Modification not allowed")
        throw RuntimeException("Order with order id: ${currOrder.orderId} is in CANCELED state. Modification not allowed")
    }
}
