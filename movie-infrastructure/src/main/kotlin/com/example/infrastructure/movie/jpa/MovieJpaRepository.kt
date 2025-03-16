package com.example.infrastructure.movie.jpa

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface MovieJpaRepository: JpaRepository<MovieEntity, Long> {

    fun findByReleaseDateLessThanEqualOrderByReleaseDateAsc(now: LocalDate): List<MovieEntity>

}