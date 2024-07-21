package com.example.myproject

import com.google.gson.annotations.SerializedName

data class Specifications (
    @SerializedName("toxicity") val toxic: String?
)