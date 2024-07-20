package com.example.cineaste.Data

import android.content.Context
import com.example.cineaste.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

object MovieRepository {

    private const val tmdb_api_key : String = BuildConfig.TMDB_API_KEY

    suspend fun searchRequest(
        query: String
    ) : GetMoviesResponse?{
        return withContext(Dispatchers.IO) {
            var response = ApiAdapter.retrofit.searchMovie(query)
            val responseBody = response.body()
            return@withContext responseBody
        }
    }

    suspend fun getSimilarMovies( id: Long
    ) : GetSimilarResponse?{
        return withContext(Dispatchers.IO) {
            var response = ApiAdapter.retrofit.getSimilar(id)
            val responseBody = response.body()
            return@withContext responseBody
        }
    }


    suspend fun getUpcomingMovies(
    ) : GetMoviesResponse?{
        return withContext(Dispatchers.IO) {
            var response = ApiAdapter.retrofit.getUpcomingMovies()
            val responseBody = response.body()
            return@withContext responseBody
        }
    }

    suspend fun getMovie(id: Long
    ) : Movie?{
        return withContext(Dispatchers.IO) {
            var response = ApiAdapter.retrofit.getMovie(id)
            val responseBody = response.body()
            return@withContext responseBody
        }
    }


    fun getUpcomingMovies2(
        onSuccess: (movies: List<Movie>) -> Unit,
        onError: () -> Unit
    ) {
        ApiAdapter.retrofit.getUpcomingMovies2()
            .enqueue(object : Callback<GetMoviesResponse> {
                override fun onResponse(
                    call: Call<GetMoviesResponse>,
                    response: Response<GetMoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }
    suspend fun getFavoriteMovies(context: Context) : List<Movie> {
        return withContext(Dispatchers.IO) {
            var db = AppDatabase.getInstance(context)
            var movies = db!!.movieDao().getAll()
            return@withContext movies
        }
    }
    suspend fun deleteMovie(context: Context, movie: Movie) : String?{
        return withContext(Dispatchers.IO){
            try {
                var db = AppDatabase.getInstance(context)
                val cast = db!!.movieDao().getMovieAndCastById(movie.id)
                db!!.castDao().deleteCast(cast.cast)
                val similar = db!!.movieDao().getSimilarMoviesById(movie.id)
                val similarPairs = similar.similarMovies.map { similar -> SimilarMovies(movie.id,similar.id) }
                for(similarPair in similarPairs){
                    db!!.similarMoviesDao().deleteSimilar(similarPair)
                }
                db!!.similarMoviesDao().deleteSimilarMovies(similar.similarMovies)
                db!!.movieDao().delete(movie)

                return@withContext "Obrisano"
            }catch (error:Exception){
                return@withContext null
            }
        }
    }
    suspend fun writeFavorite(context: Context,movie: Movie) : String?{
        return withContext(Dispatchers.IO) {
            try{
                var db = AppDatabase.getInstance(context)
                movie.favourite=1
                db!!.movieDao().insertAll(movie)
                val response = ActorMovieRepository.getCast(movie.id)
                val cast = response?.cast
                if(cast != null){
                    for (castX in cast){
                        castX.fromMovieId=movie.id
                        db!!.castDao().insertAll(castX)
                    }
                }
                val similarResponse = getSimilarMovies(movie.id)
                val simiar = similarResponse?.movies
                if(simiar != null){
                    for(sm in simiar){
                        val newSM = SimilarMovies(movieId = movie.id,similarMovieId = sm.id)
                        db!!.movieDao().insertAll(sm)
                        db!!.similarMoviesDao().insert(newSM)
                    }
                }
                return@withContext "success"
            }
            catch(error:Exception){
                return@withContext null
            }
        }
    }
    suspend fun getMovieDB(context: Context, id:Long) : Movie {
        return withContext(Dispatchers.IO) {
            var db = AppDatabase.getInstance(context)
            var movie = db!!.movieDao().findById(id)
            return@withContext movie
        }
    }
    suspend fun getCastDB(context: Context, id:Long) : List<Cast> {
        return withContext(Dispatchers.IO){
            var db = AppDatabase.getInstance(context)
            var cast = db!!.movieDao().getMovieAndCastById(id)
            return@withContext cast.cast
        }
    }
    suspend fun getSimilarMoviesDB(context: Context, id:Long): List<Movie> {
        return withContext(Dispatchers.IO){
            var db = AppDatabase.getInstance(context)
            var similarMovies = db!!.movieDao().getSimilarMoviesById(id)
            return@withContext similarMovies.similarMovies
        }
    }


}