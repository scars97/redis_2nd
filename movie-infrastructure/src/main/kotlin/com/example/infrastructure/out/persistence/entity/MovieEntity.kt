package com.example.infrastructure.out.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDate

@Entity
class MovieEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    val id: Long = 0,

    val title: String,

    val thumbnail: String,

    val releaseDate: LocalDate,

    val runTime: Int,

    val genre: String,

    val rating: String,
): BaseEntity()
