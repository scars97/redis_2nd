package com.example.application.usecase

import com.example.application.dto.ReservationInfo
import com.example.application.dto.ReservationResult
import com.example.application.event.ReservationMessageEvent
import com.example.application.validator.ReservationValidator
import com.example.business.reservation.domain.Reservation
import com.example.business.reservation.service.ReservationService
import com.example.business.seat.service.SeatService
import com.example.common.lock.DistributedLockExecutor
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class ReservationUseCase(
    private val reservationService: ReservationService,
    private val seatService: SeatService,
    private val validator: ReservationValidator,
    private val lockExecutor: DistributedLockExecutor,
    private val eventPublisher: ApplicationEventPublisher
) {

    fun createReservation(info: ReservationInfo): ReservationResult {
        validator.validate(info)

        return lockExecutor.lockWithTransaction("reserve-${info.scheduleId}", 5L, 2L) {
            val seats = seatService.getSeats(info.seatIds)

            Reservation.checkExceedLimit(info.seatIds.size)

            Reservation.checkContinuousSeats(seats)

            val reservation = reservationService.createReservation(Reservation.of(info.userId))

            seatService.updateForReserve(info.seatIds, reservation.reservationId)

            // App push 이벤트 발행
            eventPublisher.publishEvent(ReservationMessageEvent.of(reservation.reservationId))

            return@lockWithTransaction ReservationResult.of(reservation, info.seatIds)
        }
    }

}