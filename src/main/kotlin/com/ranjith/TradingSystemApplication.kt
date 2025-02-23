package com.ranjith

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TradingSystemApplication

fun main(args: Array<String>) {
	runApplication<TradingSystemApplication>(*args)
}
