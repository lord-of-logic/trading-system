package com.ranjith.mapper

import com.ranjith.dto.UserDTO
import com.ranjith.entities.User
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun toDTO(user: User) = UserDTO(
        userId = user.userId,
        userName = user.userName,
        phoneNumber = user.phoneNumber,
        emailId = user.emailId,
        createdBy = user.createdBy,
        updatedBy = user.updatedBy,
        createdOn = user.createdOn,
        updatedOn = user.updatedOn
    )

    fun toEntity(dto: UserDTO) = User(
        userId = dto.userId,
        userName = dto.userName,
        phoneNumber = dto.phoneNumber,
        emailId = dto.emailId,
        createdBy = dto.createdBy,
        updatedBy = dto.updatedBy
    )
}
