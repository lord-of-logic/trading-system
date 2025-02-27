package com.ranjith.orderTypeProcessor

import com.ranjith.entities.Order
import org.slf4j.LoggerFactory

interface OrderTypeProcessor {
    companion object {
        private val log = LoggerFactory.getLogger(OrderTypeProcessor::class.java)
    }

    fun executeTradeByOrder(order: Order)
}
