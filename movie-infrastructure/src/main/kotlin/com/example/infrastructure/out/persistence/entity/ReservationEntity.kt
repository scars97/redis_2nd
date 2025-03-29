package com.example.infrastructure.out.persistence.entity

import com.example.business.reservation.domain.ReservationStatus
import jakarta.persistence.*

@Entity
class ReservationEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    var id: Long = 0,

    @Enumerated(EnumType.STRING)
    var status: ReservationStatus,

    val userId: Long
): BaseEntity() {

    constructor(status: ReservationStatus, userId: Long):
            this(0, status, userId)

}