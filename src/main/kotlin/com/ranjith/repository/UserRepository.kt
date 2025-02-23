package com.ranjith.repository

import com.ranjith.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.userId = :userId")
    fun getByUserId(userId: Long): User?
}
