package com.example.myproject

//napisite naziv vaseg paketa kao u ostalim datotekama

import android.graphics.Bitmap
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.JUnitSoftAssertions
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


// Pagination je postavljen da pretražuje do 70te stranice, zbog HTTP 429 greške (Too Many Requests).
// Kako se ne bi desilo prekoraćenje u zahtjevima web service-u, te timeout 30sek - 1min, postavljeno je ograničenje
// U pitanju je brza pretraga, te na ovaj način, jako brzo pronađe većinu potrebnih biljaka, te im omogući i prikaz slike
// Ukoliko se neke biljke, koje su u dodatnim testovima, nalaze preko 70te stranice, samo se treba u TrefleDAO.getPlantsWithFlowerColor promijeniti while uslov na veći broj page-a
@RunWith(MockitoJUnitRunner::class)
class TestS3 {
    @get:Rule
    var softAssert = JUnitSoftAssertions()

    @Mock
    lateinit var bitmap: Bitmap

    @Test
    fun fixBosiljakTest() = runBlocking{
        var fixed = TrefleDAO().fixData(    Biljka(
            naziv = "Bosiljak (Ocimum basilicum)",
            porodica = "Netacno (usnate)",
            medicinskoUpozorenje = "Može iritati kožu osjetljivu na sunce. Preporučuje se oprezna upotreba pri korištenju ulja bosiljka.",
            medicinskeKoristi = listOf(
                MedicinskaKorist.SMIRENJE,
                MedicinskaKorist.REGULACIJAPROBAVE
            ),
            profilOkusa = ProfilOkusaBiljke.BEZUKUSNO,
            jela = listOf("Salata od paradajza", "Punjene tikvice"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA),
        ))
        softAssert.assertThat(fixed.naziv).withFailMessage("T1.1 - naziv should contain \"Ocium basilicum\"").contains("Ocimum basilicum")
        softAssert.assertThat(fixed.porodica).withFailMessage("T1.2 - porodica should contain \"Lamiaceae\"").contains("Lamiaceae")
        softAssert.assertThat(fixed.medicinskoUpozorenje).withFailMessage("T1.3 - upozorenje should contain \"NIJE JESTIVO\"").contains("NIJE JESTIVO")
        softAssert.assertThat(fixed.klimatskiTipovi).withFailMessage("T1.4 - klimatskiTipovi should contain \"Umjerena\"").contains(KlimatskiTip.UMJERENA)
        softAssert.assertAll()
    }

    @Test
    fun fixEpipactisHelleborine()= runBlocking {
        var fixed = TrefleDAO().fixData(    Biljka(
            naziv = "Kruscika (Epipactis helleborine)",
            porodica = "Netacno (netacno)",
            medicinskoUpozorenje = "Može iritati kožu osjetljivu na sunce. Preporučuje se oprezna upotreba pri korištenju ulja bosiljka.",
            medicinskeKoristi = listOf(
                MedicinskaKorist.SMIRENJE,
                MedicinskaKorist.REGULACIJAPROBAVE
            ),
            profilOkusa = ProfilOkusaBiljke.BEZUKUSNO,
            jela = listOf("Salata od paradajza", "Punjene tikvice"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA),
        ))

        softAssert.assertThat(fixed.naziv).withFailMessage("T2.1 - naziv should contain \"Epipactis helleborine\"").contains("Epipactis helleborine")
        softAssert.assertThat(fixed.medicinskoUpozorenje).withFailMessage("T2.2 - upozorenje should contain \"NIJE JESTIVO\"").contains("NIJE JESTIVO")
        softAssert.assertThat(fixed.klimatskiTipovi).withFailMessage("T2.3 - klimatskiTipovi should contain \"Planinska\"").contains(KlimatskiTip.PLANINSKA)
        softAssert.assertAll()
    }

    @Test
    fun getFlowerRosaPurple()= runBlocking {
        var plants = TrefleDAO().getPlantsWithFlowerColor("purple","rosa")
        assertTrue("T3.1 - should contain \"Rosa pendulina\"",plants.find { biljka -> biljka.naziv.contains("Rosa pendulina",ignoreCase = true) }!=null)
    }

    @Test
    fun getFlowerRampionBlue()= runBlocking {
        var plants = TrefleDAO().getPlantsWithFlowerColor("blue","rampion")
        assertTrue("T4.1 - should contain \"Phyteuma spicatum\"",plants.find { biljka -> biljka.naziv.contains("Phyteuma spicatum",ignoreCase = true) }!=null)
    }
}