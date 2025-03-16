package com.example.api.movie

import com.example.application.movie.MovieUseCase
import com.example.application.movie.dto.MovieResult
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
    fun getMovies() : ResponseEntity<List<MovieResult>> {
        return ResponseEntity.ok(movieUseCase.getAvailableMovies())
    }

}