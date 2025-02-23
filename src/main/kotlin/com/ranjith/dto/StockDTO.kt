package com.ranjith.dto

import java.time.LocalDateTime

data class StockDTO(
    var stockId: Long?,
    var stockSymbol: String?,
    var stockName: String?,
    var stockPrice: Double?,
    var createdBy: String?,
    var updatedBy: String?,
    var createdOn: LocalDateTime?,
    var updatedOn: LocalDateTime?
)
