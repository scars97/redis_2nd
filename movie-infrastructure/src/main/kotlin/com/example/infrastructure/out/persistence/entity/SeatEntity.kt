package com.example.infrastructure.out.persistence.entity

import com.example.business.seat.domain.SeatStatus
import com.example.common.exception.BusinessException
import com.example.common.exception.ErrorCode
import jakarta.persistence.*

@Entity
class SeatEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    var id: Long = 0,

    val seatNumber: String,

    @Enumerated(EnumType.STRING)
    var status: SeatStatus,

    var reservationId: Long?,

    val scheduleId: Long,

    /*@Version
    var version: Long*/
): BaseEntity() {

    constructor(seatNumber: String, status: SeatStatus, reservationId: Long?, scheduleId: Long) :
            this(0, seatNumber, status, reservationId, scheduleId/*, 0*/)

    fun reserveBy(reservationId: Long?) {
        if (this.status != SeatStatus.AVAILABLE || this.reservationId != null) {
            throw BusinessException(ErrorCode.ALREADY_RESERVED, "예약된 좌석입니다.")
        }

        this.status = SeatStatus.RESERVED
        this.reservationId = reservationId
    }

}