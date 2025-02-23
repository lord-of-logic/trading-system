package com.ranjith.dto

import com.ranjith.enums.TradeType
import java.time.LocalDateTime

data class TradeDTO(
    var tradeId: Long?,
    var tradeType: TradeType?,
    var buyerOrderId: Long?,
    var sellerOrderId: Long?,
    var stockId: Long?,
    var quantity: Int?,
    var price: Double?,
    var tradeTimestamp: LocalDateTime?,
    var createdBy: String?,
    var updatedBy: String?,
    var createdOn: LocalDateTime?,
    var updatedOn: LocalDateTime?
)
