package com.example.myproject

import androidx.room.TypeConverter

class KlimatskiTipConvert {
    @TypeConverter
    fun fromList (klimTipovi: List<KlimatskiTip>): String{
        return klimTipovi.joinToString(", ") {
            it.name
        }
    }

    @TypeConverter
    fun toList (podaci: String): List<KlimatskiTip> {
        if(podaci.isEmpty()){
            return mutableListOf()
        }
        return podaci.split(", ").map {
            KlimatskiTip.valueOf(it)
        }
    }
}