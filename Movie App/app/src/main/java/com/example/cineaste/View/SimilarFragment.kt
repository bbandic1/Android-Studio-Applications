package com.example.cineaste.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cineaste.Adapters.SimpleSimilarStringAdapter
import com.example.cineaste.Data.GetSimilarResponse
import com.example.cineaste.Data.Movie
import com.example.cineaste.Data.MovieRepository
import com.example.cineaste.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class SimilarFragment() : Fragment() {

    private lateinit var similarRV:RecyclerView
    private var movieList= listOf<Movie>()
    private lateinit var similarRVSimpleAdapter: SimpleSimilarStringAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view:View = inflater.inflate(R.layout.fragment_similar, container, false)
        val intent = requireActivity().intent
        val extras = intent.extras

        if (extras != null) {
            if (extras.containsKey("movie_id")){
                getSimilarMoviesById(extras.getLong("movie_id"))
            }
        }

        similarRV = view.findViewById<RecyclerView>(R.id.listSimilar)
        similarRV.layoutManager = LinearLayoutManager(activity)
        similarRVSimpleAdapter = SimpleSimilarStringAdapter(movieList)
        similarRV.adapter = similarRVSimpleAdapter
        return view
    }
    fun getSimilarMoviesById(query: Long){

        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch{
            val result = MovieRepository.getSimilarMovies(query)
            when (result) {
                is GetSimilarResponse -> onSuccess(result.movies)
                else-> onError()
            }
        }
    }
    fun onSuccess(movies: List<Movie>){
        movieList=movies
        similarRVSimpleAdapter.list = movies;
        similarRVSimpleAdapter.notifyDataSetChanged();
    }
    fun onError() {
        val toast = Toast.makeText(context, "Search error", Toast.LENGTH_SHORT)
        toast.show()
    }

}
