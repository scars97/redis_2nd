package com.example.infrastructure.out.persistence.entity

import com.example.business.seat.domain.SeatStatus
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

    val scheduleId: Long
): BaseEntity() {

    constructor(seatNumber: String, status: SeatStatus, reservationId: Long?, scheduleId: Long) :
            this(0, seatNumber, status, reservationId, scheduleId)

}