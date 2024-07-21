package com.example.myproject

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import kotlin.io.encoding.ExperimentalEncodingApi

class BitmapConvert {
    @OptIn(ExperimentalEncodingApi::class)
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): String {
        val izlaz = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, izlaz)
        val sekvenca = izlaz.toByteArray()
        return Base64.encodeToString(sekvenca, Base64.DEFAULT)
    }

    @OptIn(ExperimentalEncodingApi::class)
    @TypeConverter
    fun toBitmap(encodedString: String): Bitmap {
        val sekvenca = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(sekvenca, 0, sekvenca.size)
    }
}