package com.example.infrastructure.`in`.controller

import com.example.application.usecase.MovieUseCase
import com.example.infrastructure.`in`.dto.AvailableMovieResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/movies")
class MovieController(
    private val movieUseCase: MovieUseCase
) {

    @GetMapping("")
    fun getMovies() : ResponseEntity<List<AvailableMovieResponse>> {
        val movies = movieUseCase.getAvailableMovies()

        return ResponseEntity.ok(movies.map { AvailableMovieResponse.of(it) }.toList())
    }
}