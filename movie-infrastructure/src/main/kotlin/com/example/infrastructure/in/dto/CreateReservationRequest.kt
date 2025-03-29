package com.example.infrastructure.`in`.dto

import com.example.application.dto.ReservationInfo
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class CreateReservationRequest(
    @field:NotNull(message = "회원 ID는 필수 입력 값입니다.")
    @field:Positive(message = "회원 ID 는 0보다 큰 값이어야 합니다.")
    val userId: Long,

    @field:NotNull(message = "상영일정 ID는 필수 입력 값입니다.")
    @field:Positive(message = "상영일정 ID 는 0보다 큰 값이어야 합니다.")
    val scheduleId: Long,

    @field:NotNull(message = "좌석 ID는 필수 입력 값입니다.")
    @field:Size(min = 1, max = 5, message = "좌석 ID는 최소 1개 이상, 최대 5개 이하이어야 합니다.")
    val seatIds: List<@Positive(message = "좌석 ID 는 0보다 큰 값이어야 합니다.") Long> = listOf()
) {

    fun toInfo(): ReservationInfo {
        return ReservationInfo(
            userId = this.userId,
            scheduleId = this.scheduleId,
            seatIds = this.seatIds
        )
    }

}
