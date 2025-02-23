package com.ranjith.entities

import com.ranjith.enums.TradeType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@Entity
@Table(name = "trades")
data class Trade (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var tradeId: Long? = null,

    @Column(name = "trade_type")
    @Enumerated(EnumType.STRING)
    var tradeType: TradeType? = null,

    @ManyToOne
    @JoinColumn(name = "buyer_order_id")
    var buyerOrder: Order? = null,

    @ManyToOne
    @JoinColumn(name = "seller_order_id")
    var sellerOrder: Order? = null,

    @OneToOne
    @JoinColumn(name = "stock_id")
    var stock: Stock? = null,

    @Column(name = "quantity")
    var quantity: Int? = null,

    @Column(name = "price")
    var price: Double? = null,

    @Column(name = "trade_timestamp")
    var tradeTimestamp: LocalDateTime? = null,

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
