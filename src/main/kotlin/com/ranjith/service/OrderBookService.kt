package com.ranjith.service

import com.ranjith.entities.Order
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class OrderBookService {
    private val orderBook = ConcurrentHashMap<String, List<Order>>()

    fun addToOrderBook(order: Order) {
        val orders = orderBook.getOrDefault(order.stock!!.stockSymbol, emptyList())
        orderBook[order.stock!!.stockSymbol!!] = orders + order
    }

    fun removeFromOrderBook(order: Order) {
        val orders = orderBook[order.stock!!.stockSymbol] ?: throw RuntimeException("Order not found for stock ${order.stock!!.stockSymbol}")
        orderBook[order.stock!!.stockSymbol!!] = orders - order
    }

    fun getOrdersForStock(stockSymbol: String): List<Order> {
        return orderBook.getOrDefault(stockSymbol, emptyList())
    }
}
