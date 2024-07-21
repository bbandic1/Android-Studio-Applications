package com.example.myproject

import androidx.room.TypeConverter

class JelaConvert{
    @TypeConverter
    fun fromList(jelolista: List<String>): String {
        return jelolista.joinToString(", ")
    }

    @TypeConverter
    fun toList(podaci: String): List<String>{
        return podaci.split(", ")
    }
}