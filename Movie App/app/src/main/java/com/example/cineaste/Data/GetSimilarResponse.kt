package com.example.cineaste.Data

import com.google.gson.annotations.SerializedName

data class GetSimilarResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val movies: List<Movie>
)