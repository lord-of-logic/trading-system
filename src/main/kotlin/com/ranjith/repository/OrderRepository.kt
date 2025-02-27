package com.ranjith.repository

import com.ranjith.entities.Order
import com.ranjith.enums.OrderType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface OrderRepository : JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.orderId = :orderId")
    fun getByOrderId(@Param("orderId") orderId: Long): Order?

    @Query("SELECT o FROM Order o WHERE o.stock.stockId = :stockId AND o.orderType = :orderType AND o.orderStatus = 'ACCEPTED' AND o.orderAcceptedAt >= :earliestAcceptedAtTimestamp")
    fun getAcceptedOrdersByStockIdAndOrderTypeFromLastThirtyMinutes(
        @Param("stockId") stockId: Long,
        @Param("orderType") orderType: OrderType,
        @Param("earliestAcceptedAtTimestamp") earliestAcceptedAtTimestamp: LocalDateTime
    ): List<Order>
}
