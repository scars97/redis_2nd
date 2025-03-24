package com.example.infrastructure.`in`.controller

import com.example.application.usecase.MovieUseCase
import com.example.infrastructure.`in`.dto.AvailableMovieResponse
import com.example.infrastructure.`in`.dto.MovieSearchRequest
import org.springframework.http.ResponseEntity
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/movies")
class MovieController(
    private val movieUseCase: MovieUseCase
) {

    @GetMapping("")
    fun getMovies(
        @Valid @ModelAttribute request: MovieSearchRequest
    ): ResponseEntity<List<AvailableMovieResponse>> {
        val movies = movieUseCase.getAvailableMovies(request.title, request.genre)

        return ResponseEntity.ok(movies.map { AvailableMovieResponse.of(it) }.toList())
    }

}