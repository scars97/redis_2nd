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
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

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
        assertThat(result.seatIds).hasSize(3)
            .extracting("seatId")
            .containsExactly(tuple(1L), tuple(2L), tuple(3L))
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

    @DisplayName("10명이 동시에 같은 좌석을 예약하는 경우, 1명만 예약에 성공한다.")
    @Test
    fun when10UserTryToReserveTheSameSeat_thenOnly1Succeed() {
        // given
        val totalUsers = 10
        for (i in 1 until totalUsers) {
            val userName = "user" + (i + 1)
            userJpaRepository.save(UserEntity(userName))
        }
        for (i in 1..5) {
            seatJpaRepository.save(SeatEntity("A".plus(i), SeatStatus.AVAILABLE, null, schedule.id))
        }

        val executor = Executors.newFixedThreadPool(totalUsers)
        val latch = CountDownLatch(totalUsers)

        val successCount = AtomicInteger(0)
        val failureCount = AtomicInteger(0)

        // when
        for (i in 0 until totalUsers) {
            val userId = i + 1
            executor.submit {
                try {
                    sut.createReservation(ReservationInfo(userId.toLong(), 1L, listOf(1L, 2L, 3L)))
                    successCount.incrementAndGet()
                } catch (e: Exception) {
                    failureCount.incrementAndGet()
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        executor.shutdown()

        // then
        assertThat(successCount.get()).isOne()
        assertThat(failureCount.get()).isEqualTo(9)

        val reservations = reservationJpaRepository.findAll()
        assertThat(reservations.size).isOne()

        val seats = seatJpaRepository.findAllById(listOf(1L, 2L, 3L))
        assertThat(seats).hasSize(3)
            .extracting("status", "reservationId")
            .containsExactly(
                tuple(SeatStatus.RESERVED, reservations[0].id),
                tuple(SeatStatus.RESERVED, reservations[0].id),
                tuple(SeatStatus.RESERVED, reservations[0].id)
            )
    }

}