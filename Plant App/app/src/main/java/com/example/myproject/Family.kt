package com.example.myproject

import com.google.gson.annotations.SerializedName

data class Family(
    @SerializedName("name") val porodicnoIme: String
)
