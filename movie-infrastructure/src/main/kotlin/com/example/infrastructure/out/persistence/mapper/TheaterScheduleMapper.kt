package com.example.infrastructure.out.persistence.mapper

import com.example.business.theater.domain.TheaterSchedule
import com.example.infrastructure.out.persistence.entity.TheaterScheduleEntity

class TheaterScheduleMapper {

    companion object {
        fun toSchedule(entity: TheaterScheduleEntity): TheaterSchedule {
            return TheaterSchedule(
                scheduleId = entity.id,
                movieId = entity.movie.id,
                theaterId = entity.theater.id,
                screeningDate = entity.screeningDate,
                startTime = entity.startTime,
                endTime = entity.endTime
            )
        }
    }

}