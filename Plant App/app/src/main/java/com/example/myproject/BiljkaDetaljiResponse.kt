package com.example.myproject

import com.google.gson.annotations.SerializedName

data class BiljkaDetaljiResponse(
    @SerializedName("data")
    val detalji: BiljkaDetalji
){
    fun getDetaljiBiljke(): BiljkaDetalji{
        return detalji
    }
}
