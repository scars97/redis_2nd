package com.example.infrastructure.out.persistence.repository

import com.example.business.movie.domain.Movie
import com.example.business.movie.repository.MovieRepository
import com.example.infrastructure.out.persistence.entity.MovieEntity
import com.example.infrastructure.out.persistence.entity.QMovieEntity.*
import com.example.infrastructure.out.persistence.mapper.MovieMapper
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDate

@Repository
class MovieCoreRepositoryImpl(
    private val queryFactory: JPAQueryFactory
): MovieRepository {

    override fun getMoviesReleasedUntil(title: String?, genre: String?): List<Movie> {
        return queryFactory
            .select(
                Projections.constructor(MovieEntity::class.java,
                    movieEntity.id,
                    movieEntity.title,
                    movieEntity.thumbnail,
                    movieEntity.releaseDate,
                    movieEntity.runTime,
                    movieEntity.genre,
                    movieEntity.rating
                )
            )
            .from(movieEntity)
            .where(
                titleContain(title),
                genreEq(genre),
                movieEntity.releaseDate.loe(LocalDate.now())
            )
            .orderBy(movieEntity.releaseDate.asc())
            .fetch()
            .map { MovieMapper.toMovie(it) }
    }

    private fun titleContain(title: String?): BooleanExpression? {
        return if (!title.isNullOrBlank()) {
            Expressions.numberTemplate(
                BigDecimal::class.java,
                "function('MATCH', {0}, {1})",
                movieEntity.title,
                title
            ).gt(0)
        } else {
            null
        }
    }

    private fun genreEq(genre: String?): BooleanExpression? {
        val movie = movieEntity
        return if (!genre.isNullOrBlank()) {
            movie.genre.eq(genre)
        } else {
            null
        }
    }

}