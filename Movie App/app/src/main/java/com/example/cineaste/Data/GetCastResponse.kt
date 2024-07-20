package com.example.cineaste.Data

import com.example.cineaste.Data.Cast
import com.google.gson.annotations.SerializedName

data class GetCastResponse(
    @SerializedName("id") val page: Int,
    @SerializedName("cast") val cast: List<Cast>
)
