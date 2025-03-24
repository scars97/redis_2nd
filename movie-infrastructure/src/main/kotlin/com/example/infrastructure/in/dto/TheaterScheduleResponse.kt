package com.example.infrastructure.`in`.dto

import com.example.application.dto.TheaterScheduleResult
import com.example.business.theater.domain.TheaterSchedule
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import java.time.LocalTime

data class TheaterScheduleResponse(
    val scheduleId: Long,
    val movieId: Long,
    val theaterId: Long,
    val screeningDate: LocalDate,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    val startTime: LocalTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    val endTime: LocalTime
) {

    companion object {
        fun of(schedule: TheaterScheduleResult): TheaterScheduleResponse {
            return TheaterScheduleResponse(
                scheduleId = schedule.scheduleId,
                movieId = schedule.movieId,
                theaterId = schedule.theaterId,
                screeningDate = schedule.screeningDate,
                startTime = schedule.startTime,
                endTime = schedule.endTime
            )
        }
    }

}
