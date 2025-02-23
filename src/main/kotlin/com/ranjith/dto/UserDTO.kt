package com.ranjith.dto

import java.time.LocalDateTime

data class UserDTO(
    var userId: Long?,
    var userName: String?,
    var phoneNumber: String?,
    var emailId: String?,
    var createdBy: String?,
    var updatedBy: String?,
    var createdOn: LocalDateTime?,
    var updatedOn: LocalDateTime?
)
