package com.example.infrastructure.reservation.jpa

import com.example.infrastructure.common.BaseEntity
import com.example.infrastructure.seat.jpa.SeatEntity
import com.example.infrastructure.user.jpa.UserEntity
import jakarta.persistence.*

@Entity
class ReservationEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    val status: ReservationStatus,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val seat: SeatEntity
): BaseEntity()