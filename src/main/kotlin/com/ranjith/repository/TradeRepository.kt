package com.ranjith.repository

import com.ranjith.entities.Trade
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TradeRepository: JpaRepository<Trade, Long> {
}
