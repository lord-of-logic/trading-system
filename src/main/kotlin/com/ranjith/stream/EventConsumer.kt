package com.ranjith.stream

import com.ranjith.dto.ExecuteTradeEventDTO
import com.ranjith.service.OrderService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.function.Consumer

@Component
class EventConsumer(
    private val orderService: OrderService
) {
    companion object {
        private var log = LoggerFactory.getLogger(EventProducer::class.java)
    }

    @Bean
    fun executeTradeEventConsumer(): Consumer<ExecuteTradeEventDTO> {
        return Consumer {
            log.info("Consuming event for executing trade fo order Id: ${it.orderId}")
            orderService.processExecuteTrade(it)
        }
    }
}
