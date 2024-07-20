package com.example.cineaste.Data


import androidx.room.*

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie WHERE favourite=1")
    suspend fun getAll(): List<Movie>

    @Query("SELECT * FROM movie WHERE id=:id AND favourite=1 LIMIT 1")
    suspend fun findById(id: Long): Movie

    @Insert
    suspend fun insertAll(vararg movies: Movie)

    @Delete
    suspend fun delete(movie: Movie)

    @Transaction
    @Query("SELECT * FROM movie")
    suspend fun getMovieAndCast():List<MovieWithCast>

    @Transaction
    @Query("SELECT * FROM movie WHERE id=:id LIMIT 1")
    suspend fun getMovieAndCastById(id:Long): MovieWithCast

    @Transaction
    @Query("SELECT * FROM movie WHERE id=:id LIMIT 1")
    suspend fun getSimilarMoviesById(id:Long): MovieWithSimilarMovies

}