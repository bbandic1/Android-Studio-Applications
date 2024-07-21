package com.example.myproject

import androidx.room.TypeConverter

class MedicinskaKoristConvert {
    @TypeConverter
    fun fromList (medKoristi: List<MedicinskaKorist>): String{
        return medKoristi.joinToString(", ") {
            it.name
        }
    }

    @TypeConverter
    fun toList (podaci: String): List<MedicinskaKorist> {
        return podaci.split(", ").map {
            MedicinskaKorist.valueOf(it)
        }
    }
}