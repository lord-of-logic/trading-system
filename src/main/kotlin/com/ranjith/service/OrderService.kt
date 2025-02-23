package com.ranjith.service

import com.ranjith.dto.OrderDTO
import com.ranjith.dto.OrderStatusResponseDTO
import com.ranjith.dto.PlaceOrderResponseDTO
import com.ranjith.enums.OrderStatus
import com.ranjith.mapper.OrderMapper
import com.ranjith.orderStatusProcessor.OrderStatusProcessorFactory
import com.ranjith.orderTypeProcessor.OrderTypeProcessorFactory
import com.ranjith.repository.OrderRepository
import com.ranjith.repository.StockRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Service
class OrderService {
    companion object {
        private var log  = LoggerFactory.getLogger(OrderService::class.java)
    }

    @Autowired
    private lateinit var orderTypeProcessorFactory: OrderTypeProcessorFactory

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var orderMapper: OrderMapper

    @Autowired
    private lateinit var stockRepository: StockRepository

    @Autowired
    private lateinit var orderStatusProcessorFactory: OrderStatusProcessorFactory

    @Autowired
    private lateinit var orderBookService: OrderBookService

    private val lock = ReentrantLock()

    @Transactional
    fun getOrderDetailsByOrderId(orderId: Long): OrderDTO {
        log.info("Fetching order details for order id: $orderId")
        val order = orderRepository.getByOrderId(orderId) ?: throw RuntimeException("Order not found for order id: $orderId")
        return orderMapper.toDTO(order)
    }

    @Transactional
    fun getOrderStatusByOrderId(orderId: Long): OrderStatusResponseDTO {
        log.info("Fetching order status for order id: $orderId")
        val order = orderRepository.getByOrderId(orderId) ?: throw RuntimeException("Order not found for order id: $orderId")
        return OrderStatusResponseDTO(
            order.orderId!!,
            order.orderStatus ?: throw RuntimeException("Order status not found for order id: $orderId")
        )
    }

    @Transactional
    fun placeOrder(orderDTO: OrderDTO): PlaceOrderResponseDTO {
        lock.withLock {
            if(isPlaceOrderRequestValid(orderDTO))
                orderDTO.orderStatus = OrderStatus.ACCEPTED
            else
                orderDTO.orderStatus = OrderStatus.REJECTED
            orderDTO.remainingQuantity = orderDTO.originalQuantity
            val order = orderMapper.toEntity(orderDTO)
            val savedOrder = orderRepository.save(order)
            orderBookService.addToOrderBook(savedOrder)
            //ToDo: Add Event based asynchronous processing for trade execution
            val orderTypeProcessor = orderTypeProcessorFactory.getOrderTypeProcessor(order.orderType!!)
            orderTypeProcessor.executeTradeByStockId(order.stock!!.stockId!!, order)
            return PlaceOrderResponseDTO(savedOrder.orderId!!, savedOrder.orderStatus!!)
        }
    }

    private fun isPlaceOrderRequestValid(orderDTO: OrderDTO): Boolean {
        if(orderDTO.stockDTO == null)
            return false
        if(orderDTO.stockDTO!!.stockSymbol == null)
            return false
        if(orderDTO.userDTO == null)
            return false
        if(stockRepository.getByStockSymbol(orderDTO.stockDTO!!.stockSymbol!!) == null)
            return false
        if(orderDTO.originalQuantity == null || orderDTO.originalQuantity!! <= 0)
            return false
        return true
    }

    @Transactional
    fun cancelOrderByOrderId(orderId: Long): OrderStatusResponseDTO {
        lock.withLock {
            log.info("Cancelling order for order id: $orderId")
            val order = orderRepository.getByOrderId(orderId) ?: throw RuntimeException("Order not found for order id: $orderId")
            val orderStatusProcessor = orderStatusProcessorFactory.getOrderStatusProcessor(order.orderStatus!!)
            val savedOrder = orderStatusProcessor.cancelOrder(order)
            orderBookService.removeFromOrderBook(savedOrder)
            return OrderStatusResponseDTO(savedOrder.orderId!!, savedOrder.orderStatus!!)
        }
    }

    @Transactional
    fun modifyOrder(orderId: Long, orderDTO: OrderDTO): OrderDTO {
        lock.withLock {
            log.info("Modifying order for order id: $orderId")
            val currOrder = orderRepository.getByOrderId(orderId) ?: throw RuntimeException("Order not found for order id: $orderId")
            val orderStatusProcessor = orderStatusProcessorFactory.getOrderStatusProcessor(currOrder.orderStatus!!)
            val savedOrder = orderStatusProcessor.modifyOrder(currOrder, orderDTO)
            orderBookService.removeFromOrderBook(savedOrder)
            orderBookService.addToOrderBook(savedOrder)
            //ToDo: Add Event based asynchronous processing for trade execution
            val orderTypeProcessor = orderTypeProcessorFactory.getOrderTypeProcessor(savedOrder.orderType!!)
            orderTypeProcessor.executeTradeByStockId(savedOrder.stock!!.stockId!!, savedOrder)
            return orderMapper.toDTO(savedOrder)
        }
    }
}
