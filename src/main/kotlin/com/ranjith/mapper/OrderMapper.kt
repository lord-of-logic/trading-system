package com.ranjith.mapper

import com.ranjith.dto.OrderDTO
import com.ranjith.entities.Order
import com.ranjith.repository.StockRepository
import com.ranjith.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class OrderMapper {

    @Autowired
    private lateinit var userMapper: UserMapper

    @Autowired
    private lateinit var stockMapper: StockMapper

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var stockRepository: StockRepository

    fun toDTO(order: Order) = OrderDTO(
        orderId = order.orderId,
        userDTO = userMapper.toDTO(order.user ?: throw RuntimeException("User cannot be null")),
        orderType = order.orderType,
        stockDTO = stockMapper.toDTO(order.stock ?: throw RuntimeException("Stock cannot be null")),
        originalQuantity = order.originalQuantity,
        remainingQuantity = order.remainingQuantity,
        price = order.price,
        orderAcceptedAt = order.orderAcceptedAt,
        orderStatus = order.orderStatus,
        createdBy = order.createdBy,
        updatedBy = order.updatedBy,
        createdOn = order.createdOn,
        updatedOn = order.updatedOn
    )

    fun toEntity(dto: OrderDTO) = Order(
        orderId = dto.orderId,
        user = userRepository.getByUserId(dto.userDTO?.userId ?: throw RuntimeException("User cannot be null")),
        orderType = dto.orderType,
        stock = stockRepository.getByStockId(dto.stockDTO?.stockId ?: throw RuntimeException("Stock cannot be null")),
        originalQuantity = dto.originalQuantity,
        remainingQuantity = dto.remainingQuantity,
        price = dto.price,
        orderAcceptedAt = dto.orderAcceptedAt,
        orderStatus = dto.orderStatus,
        createdBy = dto.createdBy,
        updatedBy = dto.updatedBy
    )
}
