package com.example.infrastructure.`in`.dto

import com.example.application.dto.TheaterResult

data class TheaterResponse(
    val theaterId: Long,
    val name: String
) {

    companion object {
        fun of(theater: TheaterResult): TheaterResponse {
            return TheaterResponse(
                theaterId = theater.theaterId,
                name = theater.name
            )
        }
    }

}
