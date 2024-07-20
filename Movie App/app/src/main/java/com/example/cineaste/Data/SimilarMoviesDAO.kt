package com.example.cineaste.Data

import androidx.room.*
import com.example.cineaste.Data.Movie
import com.example.cineaste.Data.SimilarMovies

@Dao
interface SimilarMoviesDAO {
    @Insert(onConflict=OnConflictStrategy.IGNORE)
    suspend fun insert(join: SimilarMovies)

    @Transaction
    @Delete
    suspend fun deleteSimilar(join: SimilarMovies)

    @Transaction
    @Delete
    suspend fun deleteSimilarMovies(smovies:List<Movie>)

}