package com.example.cineaste.Data

import com.google.gson.annotations.SerializedName
import androidx.room.*

@Entity
data class Cast (
    @PrimaryKey @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    var fromMovieId: Long
) {
}