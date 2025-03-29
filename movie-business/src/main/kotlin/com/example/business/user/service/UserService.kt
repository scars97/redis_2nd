package com.example.business.user.service

import com.example.business.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun isUserExists(userId: Long): Boolean {
        return userRepository.existsBy(userId)
    }

}