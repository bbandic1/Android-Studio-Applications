package com.example.cineaste.Data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MovieWithSimilarMovies(
    @Embedded val movie: Movie,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(value = SimilarMovies::class,
            parentColumn = "movieId",
            entityColumn = "similarMovieId")
    )
    val similarMovies:List<Movie>
) {
}