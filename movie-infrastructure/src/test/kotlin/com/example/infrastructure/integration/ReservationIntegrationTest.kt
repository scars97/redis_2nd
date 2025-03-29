package com.example.infrastructure.integration

import com.example.application.dto.ReservationInfo
import com.example.application.usecase.ReservationUseCase
import com.example.business.seat.domain.SeatStatus
import com.example.common.exception.BusinessException
import com.example.common.exception.ErrorCode
import com.example.infrastructure.config.IntegrationTestSupport
import com.example.infrastructure.out.persistence.entity.SeatEntity
import com.example.infrastructure.out.persistence.entity.TheaterScheduleEntity
import com.example.infrastructure.out.persistence.entity.UserEntity
import com.example.infrastructure.out.persistence.repository.ReservationJpaRepository
import com.example.infrastructure.out.persistence.repository.SeatJpaRepository
import com.example.infrastructure.out.persistence.repository.TheaterScheduleJpaRepository
import com.example.infrastructure.out.persistence.repository.UserJpaRepository
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.time.LocalTime

class ReservationIntegrationTest @Autowired constructor(
    private val sut: ReservationUseCase,
    private val userJpaRepository: UserJpaRepository,
    private val scheduleJpaRepository: TheaterScheduleJpaRepository,
    private val seatJpaRepository: SeatJpaRepository,
    private val reservationJpaRepository: ReservationJpaRepository
): IntegrationTestSupport() {

    private lateinit var user: UserEntity
    private lateinit var schedule: TheaterScheduleEntity

    @BeforeEach
    fun setUp() {
        user = userJpaRepository.save(UserEntity("user1"))

        schedule = scheduleJpaRepository.save(
            TheaterScheduleEntity(
                LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(2), 1L, 1L
            )
        )
    }

    @DisplayName("예약이 정상적으로 처리되면 예약 결과를 반환한다.")
    @Test
    fun reservationIsSuccessfulThenReturnResult() {
        // given
        val info = ReservationInfo(1L, 1L, listOf(1L, 2L, 3L))
        for (i in 1..5) {
            seatJpaRepository.save(SeatEntity("A".plus(i), SeatStatus.AVAILABLE, null, schedule.id))
        }

        // when
        val result = sut.createReservation(info)

        // then
        assertThat(result).extracting("reservationId", "userId")
            .containsExactly(1L, 1L)
        assertThat(result.seats).hasSize(3)
            .extracting("seatId", "seatNumber", "status", "reservationId", "scheduleId")
            .containsExactly(
                tuple(1L, "A1", "RESERVED", 1L, 1L),
                tuple(2L, "A2", "RESERVED", 1L, 1L),
                tuple(3L, "A3", "RESERVED", 1L, 1L)
            )
    }

    @DisplayName("좌석 상태 수정 중 예외가 발생하면 이전에 생성된 예약 데이터는 rollback 된다.")
    @Test
    fun whenExceptionIsThrownThenRollback() {
        // given
        val info = ReservationInfo(1L, 1L, listOf(1L, 2L, 3L))
        for (i in 1..5) {
            seatJpaRepository.save(SeatEntity("A".plus(i), SeatStatus.RESERVED, 1L, schedule.id))
        }

        // when
        assertThatThrownBy { sut.createReservation(info) }
            .isInstanceOf(BusinessException::class.java)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ALREADY_RESERVED)

        val findAll = reservationJpaRepository.findAll()
        assertThat(findAll.size).isZero()
    }
}