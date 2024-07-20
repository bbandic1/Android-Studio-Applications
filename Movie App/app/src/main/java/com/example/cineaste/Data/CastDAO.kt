package com.example.cineaste.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Transaction
import com.example.cineaste.Data.Cast

@Dao
interface CastDao {
    @Insert
    suspend fun insertAll(vararg cast: Cast)

    @Transaction
    @Delete
    suspend fun deleteCast(cast: List<Cast>)
}