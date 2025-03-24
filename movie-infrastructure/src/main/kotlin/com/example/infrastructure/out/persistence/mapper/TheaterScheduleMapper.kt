package com.example.infrastructure.out.persistence.mapper

import com.example.business.theater.domain.TheaterSchedule
import com.example.infrastructure.out.persistence.entity.TheaterScheduleEntity

class TheaterScheduleMapper {

    companion object {
        fun toSchedule(entity: TheaterScheduleEntity): TheaterSchedule {
            return TheaterSchedule(
                scheduleId = entity.id,
                movieId = entity.movieId,
                theaterId = entity.theaterId,
                screeningDate = entity.screeningDate,
                startTime = entity.startTime,
                endTime = entity.endTime
            )
        }
    }

}