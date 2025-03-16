package com.example.business.theater.domain

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import java.time.LocalTime

data class TheaterSchedule (
    val scheduleId: Long,
    val movieId: Long,
    val theaterId: Long,
    val screeningDate: LocalDate,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    val startTime: LocalTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    val endTime: LocalTime
)