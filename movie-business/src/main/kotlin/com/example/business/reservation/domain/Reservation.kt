package com.example.business.reservation.domain

import com.example.business.seat.domain.Seat
import java.lang.IllegalArgumentException

data class Reservation(
    val reservationId: Long,
    val status: ReservationStatus,
    val userId: Long
) {

    constructor(status: ReservationStatus, userId: Long):
            this(0, status, userId)

    companion object {
        fun of(userId: Long): Reservation {
            return Reservation(
                ReservationStatus.DONE,
                userId
            )
        }

        fun checkExceedLimit(count: Int) {
            val seatLimit = 5
            if (seatLimit < count) {
                throw IllegalArgumentException("최대 5개의 좌석을 예약할 수 있습니다.")
            }
        }

        fun checkContinuousSeats(seats: List<Seat>) {
            if (seats.size == 1) return

            // 행(A, B, C...) 기준 그룹화
            val groupedSeats = seats.map { it.seatNumber }.groupBy { it.first() }

            groupedSeats.values.forEach { seatList ->
                // 단일 좌석이면 실패
                if (seatList.size == 1) {
                    throw IllegalArgumentException("2개 이상의 좌석을 예약할 때는 같은 행이어야 합니다.")
                }

                // 모든 좌석이 연속된 경우만 성공
                val numbers = seatList.map { it.drop(1).toInt() }
                if (!numbers.zipWithNext().all { (a, b) -> b - a == 1 }) {
                    throw IllegalArgumentException("각 행의 좌석은 연속되어야 합니다.")
                }
            }
        }
    }

}
