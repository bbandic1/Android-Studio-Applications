package com.example.myproject

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NovaBiljkaActivity : AppCompatActivity() {

        private lateinit var nazivET: EditText
        private lateinit var porodicaET: EditText
        private lateinit var medicinskoUpozorenjeET: EditText
        private lateinit var jeloET: EditText
        private lateinit var medicinskaKoristLV: ListView
        private lateinit var klimatskiTipLV: ListView
        private lateinit var zemljisniTipLV: ListView
        private lateinit var profilOkusaLV: ListView
        private lateinit var jelaLV: ListView
        private lateinit var dodajJeloBtn: Button
        private lateinit var dodajBiljkuBtn: Button
        private lateinit var uslikajBiljkuBtn: Button
        private lateinit var medicinskaKoristLVT: EditText
        private lateinit var klimatskiTipLVT: EditText
        private lateinit var zemljisniTipLVT: EditText
        private lateinit var profilOkusaLVT: EditText
        private var odabraniJeloPosition: Int = -1
        private lateinit var slikaIV: ImageView
        private lateinit var slika: Bitmap
        private lateinit var baza: BiljkaDatabase
        private lateinit var biljkaDAO: BiljkaDAO

        companion object{
            private const val REQUEST_IMAGE_CAPTURE=1
        }
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_nova_biljka)

            // Inicijalizacija elemenata interface-a
            initializeViews()

            // Postavljanje adaptera za ListView-ove
            setListViewAdapters()

            // Postavljanje onClickListener-a za button "Dodaj jelo"
            dodajJeloBtn.setOnClickListener {
                dodajIliIzmijeniJelo()
            }

            // Postavljanje onClickListener-a za button "Dodaj biljku"
            dodajBiljkuBtn.setOnClickListener {
                dodajBiljku()
            }

            uslikajBiljkuBtn.setOnClickListener {
                uslikajSliku()
            }

            // Inicijalizacija baze
            baza = BiljkaDatabase.getBazu(this@NovaBiljkaActivity)

            // Inicijalizacija biljkaDAO
            biljkaDAO = baza.biljkaDao()

            // Postavljanje OnItemClickListener-a za ListView jelaLV
            jelaLV.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                // Spremi poziciju odabranog elementa
                odabraniJeloPosition = position
                // Postavljanje teksta odabranog jela u EditText jeloET
                jeloET.setText(jelaLV.getItemAtPosition(position).toString())
                // Postavljanje teksta dugmeta na "Izmijeni jelo"
                dodajJeloBtn.text = "Izmijeni jelo"
            }
        }

    private fun initializeViews() {
            nazivET = findViewById(R.id.nazivET)
            porodicaET = findViewById(R.id.porodicaET)
            medicinskoUpozorenjeET = findViewById(R.id.medicinskoUpozorenjeET)
            jeloET = findViewById(R.id.jeloET)
            medicinskaKoristLV = findViewById(R.id.medicinskaKoristLV)
            klimatskiTipLV = findViewById(R.id.klimatskiTipLV)
            zemljisniTipLV = findViewById(R.id.zemljisniTipLV)
            profilOkusaLV = findViewById(R.id.profilOkusaLV)
            jelaLV = findViewById(R.id.jelaLV)
            dodajJeloBtn = findViewById(R.id.dodajJeloBtn)
            dodajBiljkuBtn = findViewById(R.id.dodajBiljkuBtn)
            uslikajBiljkuBtn = findViewById(R.id.uslikajBiljkuBtn)
            medicinskaKoristLVT = findViewById(R.id.medicinskaKoristLVT)
            klimatskiTipLVT = findViewById(R.id.klimatskiTipLVT)
            zemljisniTipLVT = findViewById(R.id.zemljisniTipLVT)
            profilOkusaLVT = findViewById(R.id.profilOkusaLVT)
            slikaIV = findViewById(R.id.slikaIV)
        }

        private fun setListViewAdapters() {
            // Postavljanje adaptera za ListView medicinskaKoristLV
            val medicinskaKoristAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                MedicinskaKorist.values().map { it.opis }
            )
            medicinskaKoristLV.adapter = medicinskaKoristAdapter

            // Postavljanje adaptera za ListView klimatskiTipLV
            val klimatskiTipAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                KlimatskiTip.values().map { it.opis }
            )
            klimatskiTipLV.adapter = klimatskiTipAdapter

            // Postavljanje adaptera za ListView zemljisniTipLV
            val zemljisniTipAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                Zemljiste.values().map { it.naziv }
            )
            zemljisniTipLV.adapter = zemljisniTipAdapter

            // Postavljanje adaptera za ListView profilOkusaLV
            val profilOkusaAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_single_choice,
                ProfilOkusaBiljke.values().map { it.opis }
            )
            profilOkusaLV.adapter = profilOkusaAdapter

            // Postaviti adapter za ListView jela (prazna lista na početku)
            val jelaAdapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                mutableListOf()
            )
            jelaLV.adapter = jelaAdapter
        }

    private fun dodajIliIzmijeniJelo() {

        // Dohvaćanje teksta iz EditText-a jeloET
        val novoJelo = jeloET.text.toString().trim().toLowerCase()

        // Dohvaćanje adaptera za listu jela
        val adapter = jelaLV.adapter as ArrayAdapter<String>

        var jeloVecPostoji = false

        // Provjera da li novo jelo već postoji u listi
        for (i in 0 until adapter.count) {
            val jelo = adapter.getItem(i)?.toLowerCase()
            if (jelo == novoJelo) {
                // Ako je pronađeno isto jelo, postavi zastavicu i prekini petlju
                jeloVecPostoji = true
                break
            }
        }

        // Ako je pronađeno isto jelo, prikaži poruku o grešci i prekini funkciju
        if (jeloVecPostoji) {
            jeloET.error = "Ne možete dodati dva ista jela"
            return
        } else {
            // Ako jelo ne postoji, resetiraj error poruku
            jeloET.error = null
        }

        if (odabraniJeloPosition != -1) {
            // Ako je odabran postojeći element
            if (novoJelo.isNotEmpty()) {
                // Ako novo jelo nije prazan string, ažuriraj ga
                adapter.remove(adapter.getItem(odabraniJeloPosition))
                adapter.insert(novoJelo, odabraniJeloPosition)
            } else {
                // Ako je novo jelo prazan string, izbriši ga
                adapter.remove(adapter.getItem(odabraniJeloPosition))
            }
            odabraniJeloPosition = -1 // Resetiraj odabranu poziciju
        } else {
            // Ako nije odabran postojeći element i ako novo jelo nije prazan string, dodaj ga u listu
            if (novoJelo.isNotEmpty()) {
                adapter.add(novoJelo)
            }
        }

        // Obavijesti adapter da je došlo do promjene u podacima
        adapter.notifyDataSetChanged()

        // Resetiraj tekst u EditText-u jeloET
        jeloET.setText("")

        // Resetiraj tekst na dugmetu na "Dodaj jelo"
        dodajJeloBtn.text = "Dodaj jelo"
    }

        @SuppressLint("SuspiciousIndentation")
        private fun dodajBiljku() {
            if (!validateFields()) {
                return
            }
            val naziv = nazivET.text.toString().trim()
            val porodica = porodicaET.text.toString().trim()
            val medicinskoUpozorenje = medicinskoUpozorenjeET.text.toString().trim()
            val jelaList = mutableListOf<String>()
            val medicinskeKoristi = mutableListOf<MedicinskaKorist>()
            val klimatskiTipovi = mutableListOf<KlimatskiTip>()
            val zemljisniTipovi = mutableListOf<Zemljiste>()
            var profilOkusa: ProfilOkusaBiljke? = null

            // Dobijanje odabranih medicinskih koristi iz ListView-a
            val adapter = medicinskaKoristLV.adapter as ArrayAdapter<String> // Pretpostavljamo da su opisi Stringovi
            val checkedItems = medicinskaKoristLV.checkedItemPositions

            for (i in 0 until adapter.count) {
                if (checkedItems[i]) { // Provjerite je li stavka označena
                    // Dohvatite opis odabrane medicinske koristi
                    val opis = adapter.getItem(i)

                    // Pronađite odgovarajuću medicinsku korist na temelju opisa
                    val odabranaMedicinskaKorist = MedicinskaKorist.values().find { it.opis == opis }

                    if (odabranaMedicinskaKorist != null) {
                        // Ako je pronađena odgovarajuća medicinska korist, dodajte je u listu
                        medicinskeKoristi.add(odabranaMedicinskaKorist)
                    } else {
                        Log.e(TAG, "Nije moguće pronaći medicinsku korist za opis: $opis")
                    }
                }
            }

            // Dobijanje odabranih klimatskih tipova iz ListView-a
            val adapterKlimatskiTip = klimatskiTipLV.adapter as ArrayAdapter<String>
            val selectedKlimatskiTipoviPositions = klimatskiTipLV.checkedItemPositions

            for (i in 0 until adapterKlimatskiTip.count) {
                if (selectedKlimatskiTipoviPositions[i]) {
                    val opisKlimatskogTipa = adapterKlimatskiTip.getItem(i)
                    val odabraniKlimatskiTip = KlimatskiTip.values().find { it.opis == opisKlimatskogTipa }

                    if (odabraniKlimatskiTip != null) {
                        klimatskiTipovi.add(odabraniKlimatskiTip)
                    } else {
                        Log.e("NovaBiljkaActivity", "Nije moguće pronaći klimatski tip za opis: $opisKlimatskogTipa")
                    }
                }
            }

            // Dobijanje odabranih zemljisnih tipova iz ListView-a
            val adapterZemljisniTip = zemljisniTipLV.adapter as ArrayAdapter<String>
            val selectedZemljisniTipoviPositions = zemljisniTipLV.checkedItemPositions

            for (i in 0 until adapterZemljisniTip.count) {
                if (selectedZemljisniTipoviPositions[i]) {
                    val opisZemljisnogTipa = adapterZemljisniTip.getItem(i)
                    val odabraniZemljisniTip = Zemljiste.values().find { it.naziv == opisZemljisnogTipa }

                    if (odabraniZemljisniTip != null) {
                        zemljisniTipovi.add(odabraniZemljisniTip)
                    } else {
                        Log.e("NovaBiljkaActivity", "Nije moguće pronaći zemljisni tip za opis: $opisZemljisnogTipa")
                    }
                }
            }

            // Dobijanje odabranog profila okusa iz ListView-a
            val adapterProfilOkusa = profilOkusaLV.adapter as ArrayAdapter<String>
            val selectedProfilOkusaPosition = profilOkusaLV.checkedItemPosition

            if (selectedProfilOkusaPosition != ListView.INVALID_POSITION) {
                val opisProfilOkusa = adapterProfilOkusa.getItem(selectedProfilOkusaPosition)
                val profilOkusaEnum = ProfilOkusaBiljke.values().find { it.opis == opisProfilOkusa }

                if (profilOkusaEnum != null) {
                    profilOkusa = profilOkusaEnum
                } else {
                    Log.e(TAG, "Nije moguće pronaći profil okusa za opis: $opisProfilOkusa")
                }
            } else {
                Log.e(TAG, "Nijedan profil okusa nije odabran")
            }

            val adapterJela = jelaLV.adapter as? ArrayAdapter<String>
            adapterJela?.let {
                for (i in 0 until it.count) {
                    val jelo = it.getItem(i)
                    jelo?.let {
                        jelaList.add(jelo)
                    }
                }
            }

            // Kreiranje nove instance biljke
            val novaBiljka = profilOkusa?.let {
                Biljka(
                    naziv = naziv,
                    porodica = porodica,
                    medicinskoUpozorenje = medicinskoUpozorenje,
                    jela = jelaList,
                    medicinskeKoristi = medicinskeKoristi,
                    klimatskiTipovi = klimatskiTipovi,
                    zemljisniTipovi = zemljisniTipovi,
                    profilOkusa = it,
                    onlineChecked = false
                )
            }

            // instanca TrefleDAO-a
            val trefleDAO = TrefleDAO()
            trefleDAO.setContext(this)

            // Kreiranje CoroutineScope-a
            val coroutineScope = CoroutineScope(Dispatchers.Main)

            coroutineScope.launch {
                novaBiljka?.let {

                    // Pokretanje posla unutar suspendirane funkcije fixData
                    val fixBiljka = trefleDAO.fixData(novaBiljka)

                    // Dodavanje fiksirane biljke u BiljkeSingleton
                    fixBiljka?.let { BiljkeSingleton.dodajNovuBiljku(fixBiljka) }

                    // Save biljke
                    biljkaDAO.saveBiljka(fixBiljka)

                    val bitmap = trefleDAO.getImage(fixBiljka)
                    bitmap?.let { it1 ->
                        biljkaDAO.addImage(biljkaDAO.getAllBiljkas().last().id,
                            it1
                        )
                    }

                }

                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

        private fun uslikajSliku() {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if(requestCode== REQUEST_IMAGE_CAPTURE && resultCode== RESULT_OK){
                slika = data?.extras?.get("data") as Bitmap
                slikaIV.setImageBitmap(slika)
            }
            super.onActivityResult(requestCode,resultCode,data)
        }

        private fun validateFields(): Boolean {
        var isValid = true

        // Validacija naziva
        if (nazivET.text.toString().trim().length !in 2..40) {
            nazivET.error = "Naziv mora imati između 2 i 40 znakova"
            isValid = false
        }

        // Validacija porodice
        if (porodicaET.text.toString().trim().length !in 2..20) {
            porodicaET.error = "Porodica mora imati između 2 i 20 znakova"
            isValid = false
        }

        // Validacija medicinskog upozorenja
        if (medicinskoUpozorenjeET.text.toString().trim().length !in 2..20) {
            medicinskoUpozorenjeET.error = "Medicinsko upozorenje mora imati između 2 i 20 znakova"
            isValid = false
        }

            // Validacija jela
            val adapterJela = jelaLV.adapter as ArrayAdapter<String>
            if (adapterJela.isEmpty) {
                jeloET.error = "Dodajte barem jedno jelo"
                isValid = false
            } else {
                val setOfJela = HashSet<String>() // Koristimo HashSet jer ne dozvoljava duplikate
                for (i in 0 until adapterJela.count) {
                    val jelo = adapterJela.getItem(i)!!.toLowerCase() // Pretvaramo u mala slova
                    if (!setOfJela.add(jelo)) {
                        // Ako jelo već postoji u setu, postavi error poruku na jelaET
                        jeloET.error = "Ne možete dodati dva ista jela"
                        isValid = false
                        break
                    }
                }
            }
            // Provjera za listu medicinskih koristi
            var isMedicinskaKoristSelected = false
            val checkedItemsMedicinskaKorist = medicinskaKoristLV.checkedItemPositions
            if (checkedItemsMedicinskaKorist.size() == 0) {
                medicinskaKoristLVT.error = "Morate izabrati barem jednu medicinsku korist"
                isValid = false
            }

            // Provjera za listu klimatskih tipova
            var isKlimatskiTipSelected = false
            val checkedItemsKlimatskiTip = klimatskiTipLV.checkedItemPositions
            if (checkedItemsKlimatskiTip.size() == 0) {
                klimatskiTipLVT.error = "Morate izabrati barem jedan klimatski tip"
                isValid = false
            }

            // Provjera za listu zemljisnih tipova
            var isZemljisniTipSelected = false
            val checkedItemsZemljisniTip = zemljisniTipLV.checkedItemPositions
            if (checkedItemsZemljisniTip.size() == 0) {
                zemljisniTipLVT.error = "Morate izabrati barem jedan zemljisni tip"
                isValid = false
            }

            // Validacija profil okusa
            if (profilOkusaLV.checkedItemPosition == ListView.INVALID_POSITION) {
                profilOkusaLVT.error="Morate izabrati profil okusa"
                isValid = false
            }
            return isValid
    }
}


