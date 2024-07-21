package com.example.myproject

import com.google.gson.annotations.SerializedName

data class BiljkaSerialized(
    @SerializedName("id") val id: Int,
    @SerializedName("family") val porodicnoIme: String,
    @SerializedName("edible") val isEdible: Boolean,
    @SerializedName("scientific_name") val scientificName: String,
    @SerializedName("common_name") val commonName: String?,
    @SerializedName("main_species") val main_species: Main_Species
)
