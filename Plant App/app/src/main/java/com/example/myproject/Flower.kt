package com.example.myproject

import com.google.gson.annotations.SerializedName

data class Flower(
    @SerializedName("color") val color: List<String>?
)
