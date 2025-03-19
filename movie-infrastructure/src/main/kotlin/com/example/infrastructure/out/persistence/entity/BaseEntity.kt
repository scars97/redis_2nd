package com.example.infrastructure.out.persistence.entity

import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity (
    @CreatedBy
    val createBy: String? = null,

    @CreatedDate
    val createAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedBy
    val modifiedBy: String? = null,

    @LastModifiedDate
    val modifiedAt: LocalDateTime = LocalDateTime.now()
)
