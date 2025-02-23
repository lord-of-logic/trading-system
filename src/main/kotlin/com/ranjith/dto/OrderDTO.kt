package com.ranjith.dto

import com.ranjith.enums.OrderStatus
import com.ranjith.enums.OrderType
import java.time.LocalDateTime

data class OrderDTO(
    var orderId: Long?,
    var userDTO: UserDTO?,
    var orderType: OrderType?,
    var stockDTO: StockDTO?,
    var originalQuantity: Int?,
    var remainingQuantity: Int?,
    var price: Double?,
    var orderAcceptedAt: LocalDateTime?,
    var orderStatus: OrderStatus?,
    var createdBy: String?,
    var updatedBy: String?,
    var createdOn: LocalDateTime?,
    var updatedOn: LocalDateTime?
)
