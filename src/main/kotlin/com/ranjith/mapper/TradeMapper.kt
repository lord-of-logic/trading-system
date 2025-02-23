package com.ranjith.mapper

import com.ranjith.dto.TradeDTO
import com.ranjith.entities.Order
import com.ranjith.entities.Stock
import com.ranjith.entities.Trade

class TradeMapper {
    fun toDTO(trade: Trade) = TradeDTO(
        tradeId = trade.tradeId,
        tradeType = trade.tradeType,
        buyerOrderId = trade.buyerOrder?.orderId,
        sellerOrderId = trade.sellerOrder?.orderId,
        stockId = trade.stock?.stockId,
        quantity = trade.quantity,
        price = trade.price,
        tradeTimestamp = trade.tradeTimestamp,
        createdBy = trade.createdBy,
        updatedBy = trade.updatedBy,
        createdOn = trade.createdOn,
        updatedOn = trade.updatedOn
    )

    fun toEntity(dto: TradeDTO, buyerOrder: Order?, sellerOrder: Order?, stock: Stock?) = Trade(
        tradeId = dto.tradeId,
        tradeType = dto.tradeType,
        buyerOrder = buyerOrder,
        sellerOrder = sellerOrder,
        stock = stock,
        quantity = dto.quantity,
        price = dto.price,
        tradeTimestamp = dto.tradeTimestamp,
        createdBy = dto.createdBy,
        updatedBy = dto.updatedBy
    )
}
