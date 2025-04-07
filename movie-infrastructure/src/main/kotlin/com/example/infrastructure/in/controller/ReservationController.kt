package com.example.infrastructure.`in`.controller

import com.example.application.usecase.ReservationUseCase
import com.example.common.ratelimit.LimitRequestPerTime
import com.example.infrastructure.`in`.dto.CreateReservationRequest
import com.example.infrastructure.`in`.dto.ReservationResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/api/reservations")
class ReservationController(
    private val reservationUseCase: ReservationUseCase
) {

    @LimitRequestPerTime(
        key = "#request.userId + '-' + #request.scheduleId",
        ttl = 5,
        ttlTimeUnit = TimeUnit.MINUTES,
        limitCount = 1L
    )
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun createReservation(
        @Valid @RequestBody request: CreateReservationRequest
    ): ResponseEntity<ReservationResponse> {
        val reservation = reservationUseCase.createReservation(request.toInfo())

        return ResponseEntity.ofNullable(ReservationResponse.of(reservation))
    }

}