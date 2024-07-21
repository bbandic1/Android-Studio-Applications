package com.example.myproject

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var biljkaDao: BiljkaDAO
    private lateinit var db: BiljkaDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, BiljkaDatabase::class.java).build()
        biljkaDao = db.biljkaDao()
    }
    @After
    fun closeDb() {
        db.close()
    }
    @Test
    fun testAddBiljka() = runBlocking {
        val biljka = Biljka(
            naziv = "test (Mentha longifolia)",
            porodica = "test",
            medicinskoUpozorenje = "test",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.REGULACIJAPROBAVE),
            profilOkusa = ProfilOkusaBiljke.BEZUKUSNO,
            jela = listOf("test"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
        )
        biljkaDao.saveBiljka(biljka)

        val allBiljkas = biljkaDao.getAllBiljkas()
        assertThat(allBiljkas.size, `is`(1))
        assertThat(allBiljkas[0].naziv, `is`("test (Mentha longifolia)"))
    }

    @Test
    fun testClearData() = runBlocking {
        val biljka = Biljka(
            naziv = "test (Mentha longifolia)",
            porodica = "test",
            medicinskoUpozorenje = "test",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.REGULACIJAPROBAVE),
            profilOkusa = ProfilOkusaBiljke.BEZUKUSNO,
            jela = listOf("test"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
        )
        biljkaDao.saveBiljka(biljka)

        biljkaDao.clearData()

        val allBiljkas = biljkaDao.getAllBiljkas()
        assertThat(allBiljkas.isEmpty(), `is`(true))
    }
}