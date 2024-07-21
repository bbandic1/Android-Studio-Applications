package com.example.myproject

import androidx.room.TypeConverter

class ZemljisteConvert {
    @TypeConverter
    fun fromList (zemljTipovi: List<Zemljiste>): String{
        return zemljTipovi.joinToString(", ") {
            it.name
        }
    }

    @TypeConverter
    fun toList (podaci: String): List<Zemljiste> {
        if(podaci.isEmpty()){
            return mutableListOf()
        }
        return podaci.split(", ").map {
            Zemljiste.valueOf(it)
        }
    }
}