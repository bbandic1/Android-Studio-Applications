package com.example.cineaste.Data

import androidx.room.Embedded
import androidx.room.Relation
import com.example.cineaste.Data.Cast
import com.example.cineaste.Data.Movie


data class MovieWithCast(
    @Embedded val movie : Movie,
    @Relation(
        parentColumn = "id",
        entityColumn = "fromMovieId"
    )
    val cast:List<Cast>
){

}