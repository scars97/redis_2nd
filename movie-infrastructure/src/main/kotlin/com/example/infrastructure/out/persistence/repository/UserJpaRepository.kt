package com.example.infrastructure.out.persistence.repository

import com.example.infrastructure.out.persistence.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository: JpaRepository<UserEntity, Long> {
}