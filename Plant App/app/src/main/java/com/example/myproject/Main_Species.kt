package com.example.myproject

import com.google.gson.annotations.SerializedName

data class Main_Species(
    @SerializedName("specifications") val specifications: Specifications,
    @SerializedName("growth") val growth: Growth,
    @SerializedName("flower") val flower: Flower
)
