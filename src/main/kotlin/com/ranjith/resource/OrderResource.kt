package com.ranjith.resource

import com.ranjith.dto.OrderDTO
import com.ranjith.dto.OrderStatusResponseDTO
import com.ranjith.dto.PlaceOrderResponseDTO
import com.ranjith.service.OrderService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/order")
class OrderResource {
    companion object {
        private val log = LoggerFactory.getLogger(OrderResource::class.java)
    }

    //DI, Singleton design pattern taken care by Spring's IoC container
    @Autowired
    private lateinit var orderService: OrderService

    @RequestMapping("/{orderId}", method = [RequestMethod.GET])
    fun getOrderDetailsByOrderId(@PathVariable("orderId") orderId: Long): OrderDTO {
        return orderService.getOrderDetailsByOrderId(orderId)
    }

    @RequestMapping("/status/{orderId}", method = [RequestMethod.GET])
    fun getOrderStatusByOrderId(@PathVariable("orderId") orderId: Long): OrderStatusResponseDTO {
        return orderService.getOrderStatusByOrderId(orderId)
    }

    @RequestMapping(method = [RequestMethod.POST])
    fun placeOrder(@RequestBody orderDTO: OrderDTO): PlaceOrderResponseDTO {
        return orderService.placeOrder(orderDTO)
    }

    @RequestMapping("/cancel/{orderId}", method = [RequestMethod.PUT])
    fun cancelOrder(@PathVariable("orderId") orderId: Long): OrderStatusResponseDTO {
        return orderService.cancelOrderByOrderId(orderId)
    }

    @RequestMapping("/modify/{orderId}", method = [RequestMethod.PUT])
    fun modifyOrder(@PathVariable("orderId") orderId: Long, @RequestBody orderDTO: OrderDTO): OrderDTO {
        return orderService.modifyOrder(orderId, orderDTO)
    }
}
