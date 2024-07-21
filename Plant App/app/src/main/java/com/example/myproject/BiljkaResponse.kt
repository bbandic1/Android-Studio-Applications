package com.example.myproject

import com.google.gson.annotations.SerializedName

data class BiljkaResponse(
    @SerializedName("data")
    val listaBiljaka: List<BiljkaSerialized>
) {
    fun getlistaBiljaka(): List<BiljkaSerialized> {
        return listaBiljaka
    }
}
