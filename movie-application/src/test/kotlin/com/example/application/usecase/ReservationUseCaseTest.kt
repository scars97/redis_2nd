package com.example.application.usecase

import com.example.application.dto.ReservationInfo
import com.example.application.event.ReservationMessageEvent
import com.example.application.validator.ReservationValidator
import com.example.business.reservation.domain.Reservation
import com.example.business.reservation.domain.ReservationStatus
import com.example.business.reservation.service.ReservationService
import com.example.business.seat.domain.Seat
import com.example.business.seat.domain.SeatStatus
import com.example.business.seat.service.SeatService
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.context.ApplicationEventPublisher

@ExtendWith(MockitoExtension::class)
class ReservationUseCaseTest {

    @InjectMocks
    private lateinit var sut: ReservationUseCase

    @Mock
    private lateinit var reservationService: ReservationService

    @Mock
    private lateinit var seatService: SeatService

    @Mock
    private lateinit var validator: ReservationValidator

    @Mock
    private lateinit var eventPublisher: ApplicationEventPublisher

    @BeforeEach
    fun setUp() {
        sut = ReservationUseCase(reservationService, seatService, validator, eventPublisher)
    }

    @DisplayName("예약 내역이 정상적으로 생성된다.")
    @Test
    fun reserve() {
        // given
        val info = ReservationInfo(1L, 1L, listOf(1L, 2L, 3L))
        val seats = mutableListOf(
            Seat(1L, "A1", SeatStatus.AVAILABLE, null, 1L),
            Seat(1L, "A2", SeatStatus.AVAILABLE, null, 1L),
            Seat(1L, "A3", SeatStatus.AVAILABLE, null, 1L)
        )
        val reservation = Reservation(1L, ReservationStatus.DONE, 1L)

        doNothing().`when`(validator).validate(info)
        doNothing().`when`(eventPublisher).publishEvent(ReservationMessageEvent.of(reservation.reservationId))

        `when`(seatService.getSeats(info.seatIds)).thenReturn(seats)
        `when`(reservationService.createReservation(Reservation.of(1L))).thenReturn(reservation)
        `when`(seatService.updateForReserve(1L, seats)).thenReturn(
            seats.map{
                it.reserveBy(reservation.reservationId)
                it
            }
        )

        // when
        val result = sut.createReservation(info)

        // then
        assertThat(result).extracting("reservationId", "userId")
            .containsExactly(1L, 1L)
        assertThat(result.seats).hasSize(3)
            .extracting("seatNumber", "status", "reservationId")
            .containsExactly(
                tuple("A1", "RESERVED", 1L),
                tuple("A2", "RESERVED", 1L),
                tuple("A3", "RESERVED", 1L),
            )
    }

}