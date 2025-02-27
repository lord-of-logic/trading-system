package com.ranjith.stream

import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Supplier

@Configuration
class EventProducer {
    companion object {
        private var log = LoggerFactory.getLogger(EventProducer::class.java)
    }

    val messageQueue = mutableListOf<Any>()

    fun send(message: Any) {
        messageQueue.add(message)
        log.info("Message added to queue: $message")
    }

    @Bean
    fun executeTradeEventProducer(): Supplier<Message<Any>?> {
        return Supplier {
            if (messageQueue.isNotEmpty()) {
                log.info("Producing event for executing trade for order id: ${messageQueue[0]}")
                val message = messageQueue.removeAt(0)
                MessageBuilder.withPayload(message).build()
            } else {
                null
            }
        }
    }
}
