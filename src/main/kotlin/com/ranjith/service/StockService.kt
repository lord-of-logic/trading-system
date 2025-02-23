package com.ranjith.service

import com.ranjith.entities.Stock
import com.ranjith.entities.Trade
import com.ranjith.repository.StockRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StockService {
    companion object {
        private val log = LoggerFactory.getLogger(StockService::class.java)
    }

    @Autowired
    private lateinit var stockRepository: StockRepository

    @Transactional
    fun updateStockPriceByTrades(stock: Stock, tradeList: List<Trade>) {
        val newStockPrice = tradeList.map { it.price!! }.average()
        stock.stockPrice = newStockPrice
        stockRepository.save(stock)
    }
}
