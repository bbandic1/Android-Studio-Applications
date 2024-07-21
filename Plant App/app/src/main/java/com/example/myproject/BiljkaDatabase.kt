package com.example.myproject

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Biljka::class, BiljkaBitmap::class], version = 1)
@TypeConverters(MedicinskaKoristConvert::class, ProfilOkusaConvert::class, JelaConvert::class, KlimatskiTipConvert::class, ZemljisteConvert::class,  BitmapConvert::class)
abstract class BiljkaDatabase : RoomDatabase() {
    abstract fun biljkaDao(): BiljkaDAO
    companion object {
        @Volatile
        private var baza: BiljkaDatabase? = null

        fun getBazu(context: Context): BiljkaDatabase {
            return baza ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BiljkaDatabase::class.java,
                    "biljke.db"
                ).build()
                baza = instance
                instance
            }
        }
    }
}