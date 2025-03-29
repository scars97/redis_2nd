package com.example.infrastructure.`in`.controller

import com.example.application.usecase.ReservationUseCase
import com.example.infrastructure.`in`.dto.CreateReservationRequest
import com.example.infrastructure.`in`.dto.ReservationResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reservations")
class ReservationController(
    private val reservationUseCase: ReservationUseCase
) {

    @PostMapping("")
    fun createReservation(
        @Valid @RequestBody request: CreateReservationRequest
    ): ResponseEntity<ReservationResponse> {
        val reservation = reservationUseCase.createReservation(request.toInfo())

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ReservationResponse.of(reservation))
    }

}