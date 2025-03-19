package com.example.application.dto

import com.example.business.theater.domain.Theater

data class TheaterResult(
    val theaterId: Long,
    val name: String
) {

    companion object {
        fun of(theater: Theater): TheaterResult {
            return TheaterResult(
                theaterId = theater.theaterId,
                name = theater.name
            )
        }
    }

}
