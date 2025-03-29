package com.example.application.dto

data class ReservationInfo(
    val userId: Long,
    val scheduleId: Long,
    val seatIds: List<Long> = listOf()
)
