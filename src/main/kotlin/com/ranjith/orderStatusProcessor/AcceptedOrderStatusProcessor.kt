package com.ranjith.orderStatusProcessor

import com.ranjith.dto.OrderDTO
import com.ranjith.entities.Order
import com.ranjith.enums.OrderStatus
import com.ranjith.repository.OrderRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AcceptedOrderStatusProcessor: OrderStatusProcessor {
    companion object {
        private val log = LoggerFactory.getLogger(AcceptedOrderStatusProcessor::class.java)
    }

    @Autowired
    private lateinit var orderRepository: OrderRepository

    override fun cancelOrder(order: Order): Order {
        log.info("Initiating cancel order with order id: ${order.orderId}")
        order.orderStatus = OrderStatus.CANCELED
        val savedOrder = orderRepository.save(order)
        return savedOrder
    }

    override fun modifyOrder(currOrder: Order, newOrderDTO: OrderDTO): Order {
        log.info("Initiating modify order with order id: ${currOrder.orderId}")
        if(isModifyOrderRequestValid(currOrder, newOrderDTO))
            currOrder.orderStatus = OrderStatus.ACCEPTED
        else
            currOrder.orderStatus = OrderStatus.REJECTED
        currOrder.originalQuantity = currOrder.originalQuantity
        currOrder.remainingQuantity = currOrder.originalQuantity
        currOrder.price = newOrderDTO.price
        val savedOrder = orderRepository.save(currOrder)
        return savedOrder
    }

    private fun isModifyOrderRequestValid(currOrder: Order, newOrderDTO: OrderDTO): Boolean {
        //We can only modify the price and quantity of the Order
        if(currOrder.stock == null)
            return false
        if(newOrderDTO.stockDTO == null)
            return false
        if(currOrder.stock!!.stockId != newOrderDTO.stockDTO!!.stockId)
            return false
        if(currOrder.user!!.userId != newOrderDTO.userDTO!!.userId)
            return false
        return true
    }
}
