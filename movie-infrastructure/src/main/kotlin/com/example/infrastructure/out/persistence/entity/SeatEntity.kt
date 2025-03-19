package com.example.infrastructure.out.persistence.entity

import com.example.infrastructure.out.persistence.enums.SeatStatus
import jakarta.persistence.*

@Entity
class SeatEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    val id: Long = 0,

    val seatNumber: String,

    @Enumerated(EnumType.STRING)
    val status: SeatStatus,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val schedule: TheaterScheduleEntity
): BaseEntity()