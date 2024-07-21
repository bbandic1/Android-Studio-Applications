package com.example.myproject

import com.google.gson.annotations.SerializedName

data class BiljkaDetalji(
    @SerializedName("id") val id: Int,
    @SerializedName("edible") val isEdible: Boolean,
    @SerializedName("main_species") val main_species: Main_Species,
    @SerializedName("scientific_name") val scientificName: String,
    @SerializedName("common_name") var commonName: String?,
    @SerializedName("family") val family: Family
)