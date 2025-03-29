package com.example.infrastructure.out.persistence.repository

import com.example.business.user.repository.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserCoreRepositoryImpl(
    private val jpaRepository: UserJpaRepository
): UserRepository {

    override fun existsBy(userId: Long): Boolean {
        return jpaRepository.existsById(userId)
    }
}