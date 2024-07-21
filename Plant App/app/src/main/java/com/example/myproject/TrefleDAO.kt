package com.example.myproject

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.room.Dao
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

@Dao
class TrefleDAO() {

    private var defaultBitmap: Bitmap? = null
    val apiKey = BuildConfig.TREFLE_API_KEY

    fun setContext(context: Context) {
        defaultBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.biljka)
    }

    private fun dajLatinIme(ime: String): String {
        val regex = Regex("""\((.*?)\)""")
        val match = regex.find(ime)
        return if (match != null) {
            //Log.d("TrefleDAO", "Izdvojeno Latinsko Ime: ${match.value}")
            match.groupValues[1]
        } else {
            //Log.d("TrefleDAO", "Nije uspjelo izdvajanje latinskog imena iz $ime")
            ""
        }
    }

    suspend fun getImage(biljka: Biljka): Bitmap? {
        val latinIme = dajLatinIme(biljka.naziv)

        if (latinIme.isEmpty()) {
            Log.d("TrefleDAO", "Nije uspjelo izdvajanje Latinskog imena iz ${biljka.naziv}")
            return defaultBitmap
        }
     //  Log.d("TrefleDAO", "Izdvojeno Latinsko Ime: $latinIme")

        val enkodiranoLatinIme = withContext(Dispatchers.IO) {
            URLEncoder.encode(latinIme, "UTF-8")
        }
      //  Log.d("TrefleDAO", "Enkodirano Latinsko Ime: $enkodiranoLatinIme")

        val url =
            URL("http://trefle.io/api/v1/species/search?q=$enkodiranoLatinIme&token=${BuildConfig.TREFLE_API_KEY}")
      //  Log.d("TrefleDAO", "URL: $url")

        return withContext(Dispatchers.IO) {
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connect()
            }
          //  Log.d("TrefleDAO", "HTTP Response Code: ${connection.responseCode}")

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                Log.d("TrefleDAO", "HTTP Response: $response")

                val jsonObject = Gson().fromJson(response, JsonObject::class.java)
                val dataArray = jsonObject.getAsJsonArray("data")
              //  Log.d("TrefleDAO", "Data Array Size: ${dataArray.size()}")

                if (dataArray.size() > 0) {
                    val biljkaObject = dataArray[0].asJsonObject
                    val imageURL = biljkaObject.get("image_url").asString
                  //  Log.d("TrefleDAO", "Image URL: $imageURL")

                    if (imageURL.isNotEmpty()) {
                        val bitmap = withContext(Dispatchers.IO) {
                            val imageConnection =
                                URL(imageURL).openConnection() as HttpURLConnection
                            imageConnection.requestMethod = "GET"
                            imageConnection.doInput = true
                            imageConnection.connect()
                            if (imageConnection.responseCode == HttpURLConnection.HTTP_OK) {
                                BitmapFactory.decodeStream(imageConnection.inputStream)
                            } else {
                                null
                            }
                        }
                        return@withContext bitmap
                    }
                }
            } else {
             //   Log.d("TrefleDAO", "HTTP Response is not OK")
            }

          //  Log.d("TrefleDAO", "Nije uspjelo dohvatanje slike za ${biljka.naziv}")
            return@withContext defaultBitmap
        }
    }

    //Klimatski
    fun KlimatskiTipovi(light: Int, atmosphericHumidity: Int): List<KlimatskiTip> {
        val mogućiTipovi = mutableListOf<KlimatskiTip>()
        if(light in 6..9 && atmosphericHumidity in 1..5) mogućiTipovi.add(KlimatskiTip.SREDOZEMNA)
        if(light in 8..10 && atmosphericHumidity in 7..10) mogućiTipovi.add(KlimatskiTip.TROPSKA)
        if(light in 6..9 && atmosphericHumidity in 5..8) mogućiTipovi.add(KlimatskiTip.SUBTROPSKA)
        if(light in 4..7 && atmosphericHumidity in 3..7 ) mogućiTipovi.add(KlimatskiTip.UMJERENA)
        if(light in 7..9 && atmosphericHumidity in 1..2) mogućiTipovi.add(KlimatskiTip.SUHA)
        if(light in 0..5 && atmosphericHumidity in 3..7 ) mogućiTipovi.add(KlimatskiTip.PLANINSKA)
        return mogućiTipovi
    }

    //Zemljišni
    fun ZemljisniTipovi(zemlja: Int): List<Zemljiste> {
        val mogućiTipovi = mutableListOf<Zemljiste>()
        if (zemlja in 1..2) mogućiTipovi.add(Zemljiste.GLINENO)
        if (zemlja in 3..4) mogućiTipovi.add(Zemljiste.PJESKOVITO)
        if (zemlja in 5..6) mogućiTipovi.add(Zemljiste.ILOVACA)
        if (zemlja in 7..8) mogućiTipovi.add(Zemljiste.CRNICA)
        if (zemlja == 9) mogućiTipovi.add(Zemljiste.SLJUNOVITO)
        if (zemlja == 10) mogućiTipovi.add(Zemljiste.KRECNJACKO)
        return mogućiTipovi
    }

    suspend fun fixData(biljka: Biljka): Biljka {
        val latinIme = dajLatinIme(biljka.naziv)
        if (latinIme.isEmpty()) {
            return biljka
        }
       // Log.d("TrefleDAO", "Izdvojeno Latinsko Ime: $latinIme")

        try {
          //  Log.d("TrefleDAO", "Koristi se API ključ: ${BuildConfig.TREFLE_API_KEY}")
            val Biljkaresponse = TrefleService.RetrofitClient.trefleService.dohvatiBiljke(
                latinIme,
                apiKey
            )
            val biljkaSerialized = Biljkaresponse.getlistaBiljaka()?.get(0)

            if (biljkaSerialized != null) {
              //  Log.d("TrefleDAO", "Biljka Serialized: ${biljkaSerialized.toString()}")

                // Porodica biljke
                if (biljkaSerialized.porodicnoIme != biljka.porodica) {
                    biljka.porodica = biljkaSerialized.porodicnoIme
                 //   Log.d("TrefleDAO", "Porodica biljke ažurirana: ${biljka.porodica}")
                }

                // Jela i medicinsko upozorenje
                val Detaljiresponse =
                    TrefleService.RetrofitClient.trefleService.dohvatiDetaljeBiljke(
                        biljkaSerialized.id,
                        apiKey
                    )
                val detaljiBiljke = Detaljiresponse.getDetaljiBiljke()

                if (detaljiBiljke.isEdible == false) {
                    biljka.jela = emptyList()
                    if (!biljka.medicinskoUpozorenje.contains("NIJE JESTIVO")) {
                        biljka.medicinskoUpozorenje = biljka.medicinskoUpozorenje + " NIJE JESTIVO"
                    //    Log.d(
                    //        "TrefleDAO",
                    //        "Medicinsko upozorenje ažurirano: ${biljka.medicinskoUpozorenje}"
                    //    )
                    }
                }

                // Medicinsko upozorenje za toksičnost
                if (detaljiBiljke.main_species.specifications?.toxic != null) {
                    if (!biljka.medicinskoUpozorenje.contains("TOKSIČNO")) {
                        biljka.medicinskoUpozorenje = biljka.medicinskoUpozorenje + " TOKSIČNO"
                     //   Log.d(
                     //       "TrefleDAO",
                     //       "Medicinsko upozorenje ažurirano: ${biljka.medicinskoUpozorenje}"
                     //  )
                    }
                }

                // Klimatski tipovi
                biljka.klimatskiTipovi = KlimatskiTipovi(
                    detaljiBiljke.main_species.growth.light?: 0,
                    detaljiBiljke.main_species.growth.atmosphericHumidity?: 0
                ).toMutableList()

                // Zemljisni tipovi
                biljka.zemljisniTipovi = ZemljisniTipovi(detaljiBiljke.main_species.growth.soilTexture?: 0).toMutableList()
            }
            return biljka
        } catch (exe: Exception) {
         //   Log.e("TrefleDAO", "Exception during fixData", exe)
         //  Log.e("TrefleDAO", "Exception message: ${exe.message}")
            return biljka
        }
    }

    suspend fun getPlantsWithFlowerColor(flowerColor: String, substr: String): List<Biljka> {
        return try {
            val list = mutableListOf<Biljka>()
            var page = 1
            try{
                while (page<70) {
                    val biljkaResponse = TrefleService.RetrofitClient.trefleService.dohvatiBiljkeSaBojom(flowerColor,apiKey,page)
                    //Log.d("TrefleDAO", page.toString())
                    page = page + 1
                    for(biljka in biljkaResponse.listaBiljaka){
                        var naziv: String = biljka.commonName + "(${biljka.scientificName}"
                        if (naziv.contains(substr, ignoreCase = true)) {
                            val responseDetalji = TrefleService.RetrofitClient.trefleService.dohvatiDetaljeBiljke(
                                biljka.id,
                                apiKey
                            ).getDetaljiBiljke()
                            list.add (
                               Biljka ( naziv = "${responseDetalji.commonName} (${responseDetalji.scientificName})",
                                        porodica = responseDetalji.family.porodicnoIme,
                                        medicinskoUpozorenje = "",
                                        medicinskeKoristi = emptyList(),
                                        profilOkusa = ProfilOkusaBiljke.LJUTO,
                                        jela = emptyList(),
                                        klimatskiTipovi = KlimatskiTipovi(
                                            responseDetalji.main_species.growth.light?: 0,
                                            responseDetalji.main_species.growth.atmosphericHumidity?:0
                                        ),
                                        zemljisniTipovi = ZemljisniTipovi(
                                            responseDetalji.main_species.growth.soilTexture?: 0
                                        )
                               )
                            )
                        }
                    }
                }
            } catch (exe: Exception) {
            //Log.d("TrefleDAO", "Ukupno ${page-1} stranica")
            }
            list
        } catch (exe: Exception) {
            //Log.e("TrefleDAO", "Izuzetak: ${exe.message}")
            emptyList()
        }
    }
}