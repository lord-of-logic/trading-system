package com.ranjith.dto

import com.ranjith.enums.OrderStatus

data class OrderStatusResponseDTO(
    var orderId: Long,
    var orderStatus: OrderStatus
)
