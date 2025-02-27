package com.ranjith.repository

import com.ranjith.entities.Order
import com.ranjith.enums.OrderType
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface OrderRepository : JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.orderId = :orderId")
    fun getByOrderId(@Param("orderId") orderId: Long): Order?

    @Query("SELECT o FROM Order o WHERE o.stock.stockId = :stockId AND o.orderType = :orderType AND o.orderStatus = 'ACCEPTED'")
    @Lock(LockModeType.OPTIMISTIC)
    fun getAcceptedOrdersByStockIdAndOrderTypeFromLastThirtyMinutes(
        @Param("stockId") stockId: Long,
        @Param("orderType") orderType: OrderType
    ): List<Order>
}
