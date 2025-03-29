package com.example.business.service

import com.example.business.seat.domain.Seat
import com.example.business.seat.domain.SeatStatus
import com.example.business.seat.repository.SeatRepository
import com.example.business.seat.service.SeatService
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class SeatServiceTest {

    @InjectMocks
    private lateinit var sut: SeatService

    @Mock
    private lateinit var seatRepository: SeatRepository

    @BeforeEach
    fun setUp() {
        sut = SeatService(seatRepository)
    }

    @DisplayName("요청한 좌석 중 예약된 좌석이 하나라도 있는 경우 예외가 발생한다.")
    @Test
    fun whenReservedSeatThenThrowException() {
        // given
        val reservationId = 1L
        val reservedSeat = Seat(3L, "A3", SeatStatus.RESERVED, null, 1L)
        val seats = mutableListOf(
            Seat(1L, "A1", SeatStatus.AVAILABLE, null, 1L),
            Seat(2L, "A2", SeatStatus.AVAILABLE, null, 1L),
            reservedSeat
        )

        // when // then
        assertThatThrownBy { sut.updateForReserve(reservationId, seats) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("예약된 좌석입니다.")
    }

}