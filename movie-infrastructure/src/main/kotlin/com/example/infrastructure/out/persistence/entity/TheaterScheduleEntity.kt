package com.example.infrastructure.out.persistence.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
class TheaterScheduleEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    val id: Long = 0,

    val screeningDate: LocalDate,

    val startTime: LocalTime,

    val endTime: LocalTime,

    val movieId: Long,

    val theaterId: Long,
): BaseEntity()
