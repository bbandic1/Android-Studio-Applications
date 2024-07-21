package com.example.myproject
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "Biljka")
data class Biljka(
    @PrimaryKey(autoGenerate = true) val id: Long?=null,
    var naziv: String,
    var porodica: String,
    var medicinskoUpozorenje: String,
    @TypeConverters(MedicinskaKoristConvert::class) var medicinskeKoristi: List<MedicinskaKorist>,
    @TypeConverters(ProfilOkusaConvert::class) var profilOkusa: ProfilOkusaBiljke,
    @TypeConverters(JelaConvert::class) var jela: List<String>,
    @TypeConverters(KlimatskiTipConvert::class) var klimatskiTipovi: List<KlimatskiTip>,
    @TypeConverters(ZemljisteConvert::class) var zemljisniTipovi: List<Zemljiste>,
    var onlineChecked: Boolean = false
)

