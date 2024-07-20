package com.example.cineaste.Data

import com.example.cineaste.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ActorMovieRepository {

    private const val tmdb_api_key : String = BuildConfig.TMDB_API_KEY


    suspend fun getCast( id: Long
    ) : GetCastResponse?{
        return withContext(Dispatchers.IO) {
            var response = ApiAdapter.retrofit.getCast(id)
            val responseBody = response.body()
            return@withContext responseBody
        }
    }

}
