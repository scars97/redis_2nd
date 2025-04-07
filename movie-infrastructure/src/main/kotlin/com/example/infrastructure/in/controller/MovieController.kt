package com.example.infrastructure.`in`.controller

import com.example.application.usecase.MovieUseCase
import com.example.common.ratelimit.LimitRequestPerTime
import com.example.infrastructure.`in`.dto.AvailableMovieResponse
import com.example.infrastructure.`in`.dto.MovieSearchRequest
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/api/movies")
class MovieController(
    private val movieUseCase: MovieUseCase
) {

    @LimitRequestPerTime(
        key = "#httpRequest.localAddr",
        ttl = 1,
        ttlTimeUnit = TimeUnit.MINUTES,
        limitCount = 50
    )
    @GetMapping("")
    fun getMovies(
        @Valid @ModelAttribute request: MovieSearchRequest,
        httpRequest: HttpServletRequest
    ): ResponseEntity<List<AvailableMovieResponse>> {
        val movies = movieUseCase.getAvailableMovies(request.title, request.genre)

        return ResponseEntity.ok(movies.map { AvailableMovieResponse.of(it) }.toList())
    }

}