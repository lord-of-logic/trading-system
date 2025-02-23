package com.ranjith.entities

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@Entity
@Table(name = "stocks")
data class Stock(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var stockId: Long? = null,

    @Column(name = "stock_symbol")
    var stockSymbol: String? = null,

    @Column(name = "stock_name")
    var stockName: String? = null,

    @Column(name = "stock_price")
    var stockPrice: Double? = null,

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
