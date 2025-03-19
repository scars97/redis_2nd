package com.example.infrastructure.out.persistence.mapper

import com.example.business.movie.domain.Movie
import com.example.infrastructure.out.persistence.entity.MovieEntity

class MovieMapper {
    companion object {
        fun toMovie(entity: MovieEntity): Movie {
            return Movie(
                movieId = entity.id,
                title = entity.title,
                thumbnail = entity.thumbnail,
                releaseDate = entity.releaseDate,
                runTime = entity.runTime,
                genre = entity.genre,
                rating = entity.rating
            )
        }
    }
}