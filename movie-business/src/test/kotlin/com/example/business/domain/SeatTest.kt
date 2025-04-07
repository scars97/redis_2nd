package com.example.business.domain

import com.example.business.seat.domain.Seat
import com.example.business.seat.domain.SeatStatus
import com.example.common.exception.BusinessException
import com.example.common.exception.ErrorCode
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class SeatTest {

    @DisplayName("좌석 예약 상태 검증 시나리오")
    @TestFactory
    fun isUnavailableSeatThenThrowException(): Collection<DynamicTest> {
        // given
        val reservationId = 1L
        val statusIsReserved = Seat(1L, "A1", SeatStatus.RESERVED, reservationId, 1L)
        val reservationIsNotNull = Seat(1L, "A1", SeatStatus.AVAILABLE, reservationId, 1L)

        // when // then
        return listOf(
            DynamicTest.dynamicTest("좌석 상태가 RESERVED인 경우 예외가 발생한다.") {
                Assertions.assertThatThrownBy { statusIsReserved.reserveBy(reservationId) }
                    .isInstanceOf(BusinessException::class.java)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ALREADY_RESERVED)
                    .hasMessage("예약된 좌석입니다.")
            },
            DynamicTest.dynamicTest("예약 ID 가 존재하는 경우 예외가 발생한다.") {
                Assertions.assertThatThrownBy { reservationIsNotNull.reserveBy(reservationId) }
                    .isInstanceOf(BusinessException::class.java)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ALREADY_RESERVED)
                    .hasMessage("예약된 좌석입니다.")
            }
        )
    }

    @DisplayName("예약 ID가 매핑되고, 좌석이 예약 상태로 변경된다.")
    @Test
    fun seatReserve() {
        // given
        val reservationId = 1L
        val seat = Seat(1L, "A1", SeatStatus.AVAILABLE, null, 1L)

        // when
        seat.reserveBy(reservationId)

        //then
        Assertions.assertThat(seat).extracting("status", "reservationId")
            .containsExactly(SeatStatus.RESERVED, reservationId)
    }

}