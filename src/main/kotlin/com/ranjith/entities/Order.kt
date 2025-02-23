package com.ranjith.entities

import com.ranjith.enums.OrderStatus
import com.ranjith.enums.OrderType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var orderId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User? = null,

    @Column(name = "order_type")
    @Enumerated(EnumType.STRING)
    var orderType: OrderType? = null,

    @ManyToOne
    @JoinColumn(name = "stock_id")
    var stock: Stock? = null,

    @Column(name = "original_quantity")
    var originalQuantity: Int? = null,

    @Column(name = "remaining_quantity")
    var remainingQuantity: Int? = null,

    @Column(name = "price")
    var price: Double? = null,

    @Column(name = "order_accepted_at")
    var orderAcceptedAt: LocalDateTime? = null,

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    var orderStatus: OrderStatus? = null,

    @Column(name = "version")
    @Version
    var version: Long? = null,

    @Column(name = "created_by")
    var createdBy: String? = null,

    @Column(name = "updated_by")
    var updatedBy: String? = null,

    @CreationTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "created_on")
    var createdOn: LocalDateTime? = LocalDateTime.now(),

    @UpdateTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "updated_on")
    var updatedOn: LocalDateTime? = LocalDateTime.now()
)
