package com.example.application.dto

import com.example.business.theater.domain.TheaterSchedule
import java.time.LocalDate
import java.time.LocalTime

data class TheaterScheduleResult(
    val scheduleId: Long,
    val movieId: Long,
    val theaterId: Long,
    val screeningDate: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
) {

    companion object {
        fun of(schedule: TheaterSchedule): TheaterScheduleResult {
            return TheaterScheduleResult(
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
