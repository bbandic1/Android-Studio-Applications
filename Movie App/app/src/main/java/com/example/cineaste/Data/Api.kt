package com.example.cineaste.Data

import com.example.cineaste.BuildConfig
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<GetMoviesResponse>


    @GET("movie/{id}")
    suspend fun getMovie(@Path("id") id:Long,
                         @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<Movie>
    @GET("movie/{id}/similar")
    suspend fun getSimilar(@Path("id") id:Long,
                           @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<GetSimilarResponse>
    @GET("movie/{id}/credits")
    suspend fun getCast(@Path("id") id:Long,
                        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<GetCastResponse>
    @GET("movie/latest")
    suspend fun getLatestMovie(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<Movie>
    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<GetMoviesResponse>
    @GET("movie/upcoming")
    fun getUpcomingMovies2(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Call<GetMoviesResponse>
}