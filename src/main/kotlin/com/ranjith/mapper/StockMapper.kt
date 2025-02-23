package com.ranjith.mapper

import com.ranjith.dto.StockDTO
import com.ranjith.entities.Stock
import org.springframework.stereotype.Component

@Component
class StockMapper {
    fun toDTO(stock: Stock) = StockDTO(
        stockId = stock.stockId,
        stockSymbol = stock.stockSymbol,
        stockName = stock.stockName,
        stockPrice = stock.stockPrice,
        createdBy = stock.createdBy,
        updatedBy = stock.updatedBy,
        createdOn = stock.createdOn,
        updatedOn = stock.updatedOn
    )

    fun toEntity(dto: StockDTO) = Stock(
        stockId = dto.stockId,
        stockSymbol = dto.stockSymbol,
        stockName = dto.stockName,
        stockPrice = dto.stockPrice,
        createdBy = dto.createdBy,
        updatedBy = dto.updatedBy
    )
}
