package com.ranjith.repository

import com.ranjith.entities.Stock
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface StockRepository: JpaRepository<Stock, Long> {

    @Query("SELECT s FROM Stock s WHERE s.stockSymbol = :stockSymbol")
    fun getByStockSymbol(@Param("stockSymbol") stockSymbol: String): Stock?

    @Query("SELECT s FROM Stock s WHERE s.stockId = :stockId")
    fun getByStockId(@Param("stockId") stockId: Long): Stock?
}
