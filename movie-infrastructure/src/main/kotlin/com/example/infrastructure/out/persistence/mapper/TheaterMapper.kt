package com.example.infrastructure.out.persistence.mapper

import com.example.business.theater.domain.Theater
import com.example.infrastructure.out.persistence.entity.TheaterEntity

class TheaterMapper {

    companion object {
        fun toTheater(entity: TheaterEntity): Theater {
            return Theater(
                theaterId = entity.id,
                name = entity.name
            )
        }
    }

}