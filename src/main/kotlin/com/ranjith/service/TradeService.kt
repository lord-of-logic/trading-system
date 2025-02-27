package com.ranjith.service

import com.ranjith.entities.Order
import com.ranjith.entities.Stock
import com.ranjith.entities.Trade
import com.ranjith.enums.OrderStatus
import com.ranjith.enums.TradeType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class TradeService {
    companion object {
        private val log = LoggerFactory.getLogger(TradeService::class.java)
    }

    @Autowired
    private lateinit var orderBookService: OrderBookService

    @Transactional
    fun executeTrade(
        stock: Stock,
        buyOrder: Order,
        sellOrder: Order,
        tradeType: TradeType,
        tradeQuantity: Int,
        ordersToUpdateAsCompleted: MutableList<Order>
    ): Trade? {
        if(sellOrder.remainingQuantity!! < tradeQuantity || buyOrder.remainingQuantity!! < tradeQuantity) {
            log.error("Trade quantity is greater than remaining quantity in either buy or sell order")
            return null
        }
        sellOrder.remainingQuantity = sellOrder.remainingQuantity!! - tradeQuantity
        buyOrder.remainingQuantity = buyOrder.remainingQuantity!! - tradeQuantity
        if (sellOrder.remainingQuantity == 0) {
            sellOrder.orderStatus = OrderStatus.COMPLETED
            orderBookService.removeFromOrderBook(sellOrder)
            ordersToUpdateAsCompleted.add(sellOrder)
        }
        if (buyOrder.remainingQuantity == 0) {
            buyOrder.orderStatus = OrderStatus.COMPLETED
            orderBookService.removeFromOrderBook(buyOrder)
            ordersToUpdateAsCompleted.add(buyOrder)
        }
        val executedTrade = Trade(
            tradeType = tradeType,
            buyerOrder = buyOrder,
            sellerOrder = sellOrder,
            stock = stock,
            quantity = tradeQuantity,
            price = sellOrder.price,
            tradeTimestamp = LocalDateTime.now(),
            createdBy = "SYSTEM",
            updatedBy = "SYSTEM"
        )
        return executedTrade
    }
}
