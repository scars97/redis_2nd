package com.example.infrastructure.out.persistence.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
class TheaterScheduleEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    val id: Long = 0,

    val screeningDate: LocalDate,

    val startTime: LocalTime,

    val endTime: LocalTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val movie: MovieEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val theater: TheaterEntity
): BaseEntity()
