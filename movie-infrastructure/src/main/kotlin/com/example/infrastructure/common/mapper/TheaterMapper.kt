package com.example.infrastructure.common.mapper

import com.example.business.theater.domain.Theater
import com.example.business.theater.domain.TheaterSchedule
import com.example.infrastructure.theater.jpa.TheaterEntity
import com.example.infrastructure.theater.jpa.TheaterScheduleEntity

class TheaterMapper {

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

        fun toTheater(entity: TheaterEntity): Theater {
            return Theater(
                theaterId = entity.id,
                name = entity.name
            )
        }

    }

}