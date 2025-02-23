package com.ranjith.orderStatusProcessor

import com.ranjith.enums.OrderStatus
import org.hibernate.query.Order
import org.springframework.stereotype.Component

@Component
class OrderStatusProcessorFactory {
    fun getOrderStatusProcessor(orderStatus: OrderStatus): OrderStatusProcessor {
        return when (orderStatus) {
            OrderStatus.CANCELED -> CanceledOrderStatusProcessor()
            OrderStatus.ACCEPTED -> AcceptedOrderStatusProcessor()
            OrderStatus.REJECTED -> RejectedOrderStatusProcessor()
            OrderStatus.COMPLETED -> CompletedOrderStatusProcessor()
            else -> throw RuntimeException("Invalid order status")
        }
    }
}
