package com.example.myproject

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "BiljkaBitmap",
    foreignKeys = [
        ForeignKey(
            entity = Biljka::class,
            parentColumns = ["id"],
            childColumns = ["idBiljke"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION,
            deferred = false
        )
    ]
)

data class BiljkaBitmap(
    @PrimaryKey(autoGenerate=true) val id: Long?=null,
    val idBiljke: Long?,
    val bitmap: String
)