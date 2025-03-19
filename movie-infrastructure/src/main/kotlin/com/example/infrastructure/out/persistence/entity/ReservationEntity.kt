package com.example.infrastructure.out.persistence.entity

import com.example.infrastructure.out.persistence.enums.ReservationStatus
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