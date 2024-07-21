package com.example.myproject

import androidx.room.TypeConverter

class ProfilOkusaConvert {
    @TypeConverter
    fun fromEnum (profil: ProfilOkusaBiljke): String{
        return profil.name
    }

    @TypeConverter
    fun toEnum (podaci: String): ProfilOkusaBiljke {
        return ProfilOkusaBiljke.valueOf(podaci)
    }
}
