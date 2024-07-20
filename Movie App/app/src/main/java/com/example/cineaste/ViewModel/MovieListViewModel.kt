package com.example.cineaste.ViewModel

import android.content.Context
import com.example.cineaste.Data.GetMoviesResponse
import com.example.cineaste.Data.Movie
import com.example.cineaste.Data.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieListViewModel(
) {

    val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun getFavorites(context: Context, onSuccess: (movies: List<Movie>) -> Unit,
                     onError: () -> Unit){
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        // Create a new coroutine on the UI thread
        scope.launch{

            // Make the network call and suspend execution until it finishes
            val result = MovieRepository.getFavoriteMovies(context)

            // Display result of the network request to the user
            when (result) {
                is List<Movie> -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }


    fun search(query: String,onSuccess: (movies: List<Movie>) -> Unit,
               onError: () -> Unit){

        // Create a new coroutine on the UI thread
        scope.launch{

            // Make the network call and suspend execution until it finishes
            val result = MovieRepository.searchRequest(query)

            // Display result of the network request to the user
            when (result) {
                is GetMoviesResponse -> onSuccess?.invoke(result.movies)
                else-> onError?.invoke()
            }
        }
    }

    fun getUpcoming( onSuccess: (movies: List<Movie>) -> Unit,
                     onError: () -> Unit){

        // Create a new coroutine on the UI thread
        scope.launch{

            // Make the network call and suspend execution until it finishes
            val result = MovieRepository.getUpcomingMovies()

            // Display result of the network request to the user
            when (result) {
                is GetMoviesResponse -> onSuccess?.invoke(result.movies)
                else-> onError?.invoke()
            }
        }
    }

    fun getUpcoming2( onSuccess: (movies: List<Movie>) -> Unit,
                      onError: () -> Unit){
        MovieRepository.getUpcomingMovies2(onSuccess,onError)
    }

}