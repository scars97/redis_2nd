package com.example.application.fixture

import com.example.business.movie.domain.Movie
import com.example.business.theater.domain.Theater
import com.example.business.theater.domain.TheaterSchedule
import java.time.LocalDate
import java.time.LocalTime

class MovieUseCaseFixture {

    fun createMovie(movieId: Long, title: String, releaseDate: LocalDate): Movie {
        return Movie(
            movieId = movieId,
            title = title,
            thumbnail = "url",
            releaseDate = releaseDate,
            runTime = 120,
            genre = "액션",
            rating = "전체 이용가"
        )
    }

    fun createTheater(theaterId: Long, name: String): Theater {
        return Theater(theaterId = theaterId, name = name)
    }

    fun createSchedule(
        scheduleId: Long, movieId: Long, theaterId: Long,
        startTime: LocalTime, endTime: LocalTime
    ): TheaterSchedule {
        return TheaterSchedule(
            scheduleId = scheduleId,
            movieId = movieId,
            theaterId = theaterId,
            screeningDate = LocalDate.now(),
            startTime = startTime,
            endTime = endTime
        )
    }

}