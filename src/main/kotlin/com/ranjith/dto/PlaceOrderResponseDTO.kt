package com.ranjith.dto

import com.ranjith.enums.OrderStatus

data class PlaceOrderResponseDTO(
    val orderId: Long,
    val orderStatus: OrderStatus
)
