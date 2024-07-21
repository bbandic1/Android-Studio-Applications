package com.example.myproject

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    var biljke = BiljkeSingleton.biljke
    private lateinit var pretragaET: EditText
    private lateinit var biljkeRV: RecyclerView
    private lateinit var resetBtn: Button
    private lateinit var brzaPretraga: Button
    lateinit var bojaSPIN: Spinner
    lateinit var spinner: Spinner
    private lateinit var bojaSPINLayout: LinearLayout
    private lateinit var pretragaLayout: LinearLayout
    private var filtriranaLista: List<Biljka>? = null
    private var isBrzaPretragaActive = false
    private lateinit var baza: BiljkaDatabase
    private lateinit var biljkaDAO: BiljkaDAO

    companion object {
        private const val NOVA_BILJKA_REQUEST_CODE = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Pronalaženje pretregaET Edit Text-a
        pretragaET = findViewById(R.id.pretragaET)

        bojaSPINLayout = findViewById(R.id.bojaSPINlayout)

        pretragaLayout=findViewById(R.id.pretragalayout)

        // Pronalaženje RecyclerView-a
        biljkeRV = findViewById(R.id.biljkeRV)

        // Pronalaženje ResetButton-a
        resetBtn = findViewById(R.id.resetBtn)

        // Pornalaženje brzaPretraga Button-a
        brzaPretraga = findViewById(R.id.brzaPretraga)

        // Postavljanje layout manager-a
        biljkeRV.layoutManager = LinearLayoutManager(this)

        // Inicijalizacija baze
        baza = BiljkaDatabase.getBazu(this@MainActivity)

        // Inicijalizacija biljkaDAO
        biljkaDAO = baza.biljkaDao()

        // Inicijalizacija adaptera
        val adapter = MedicinskiAdapter.MedicinskiAdapter(this, biljke, baza) { biljka ->
            biljkeRV.isClickable = true
            when(spinner.selectedItemPosition){
                0 -> filterMedicinskiMod(biljka)
                1 -> filterKuharskiMod(biljka)
                2 -> filterBotanickiMod(biljka)
            }
        }

        // Postavljanje adaptera na RecyclerView
        biljkeRV.adapter = adapter

        // Pronalaženje Spinner-a
        spinner= findViewById(R.id.modSpinner)

        // Pronalaženje bojaSPIN Spinnera
        bojaSPIN=findViewById(R.id.bojaSPIN)

        // Postavljanje opcija za Spinner
        val layoutOptions = listOf("Medicinski", "Kuharski", "Botanicki")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, layoutOptions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        val bojalayoutOptions = listOf("red", "blue", "yellow", "green", "orange", "purple", "brown")
        val bojaspinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bojalayoutOptions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bojaSPIN.adapter = bojaspinnerAdapter


        // Postavljanje podrazumijevane vrijednosti na "Medicinski"
        spinner.setSelection(layoutOptions.indexOf("Medicinski"))


        // Postavljanje slušača promjene odabira Spinner-a
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        bojaSPINLayout.visibility=View.GONE
                        pretragaLayout.visibility=View.GONE

                        if(filtriranaLista!=null && !isBrzaPretragaActive){

                            biljkeRV.adapter = MedicinskiAdapter.MedicinskiAdapter(this@MainActivity,
                                filtriranaLista!!, baza){
                                    biljka -> filterMedicinskiMod(biljka) }
                    }
                        else {
                            isBrzaPretragaActive = false
                            biljkeRV.adapter =
                                MedicinskiAdapter.MedicinskiAdapter(this@MainActivity, biljke, baza) {
                                        biljka -> filterMedicinskiMod(biljka)}
                    }
                    }

                    1 -> {
                        bojaSPINLayout.visibility=View.GONE
                        pretragaLayout.visibility=View.GONE

                        if(filtriranaLista!=null && !isBrzaPretragaActive){

                            biljkeRV.adapter = KuharskiAdapter.KuharskiAdapter(this@MainActivity,
                                filtriranaLista!!, baza){
                                    biljka -> filterKuharskiMod(biljka) }
                    }
                        else{
                            isBrzaPretragaActive = false
                            biljkeRV.adapter =
                                KuharskiAdapter.KuharskiAdapter(this@MainActivity, biljke, baza) {
                                        biljka -> filterKuharskiMod(biljka) }
                        }
                    }
                    2 -> {
                        bojaSPINLayout.visibility=View.VISIBLE
                        pretragaLayout.visibility=View.VISIBLE

                        if(filtriranaLista!=null && !isBrzaPretragaActive){

                            biljkeRV.adapter = BotanickiAdapter.BotanickiAdapter(this@MainActivity,
                                filtriranaLista!!, baza){
                                    biljka -> filterBotanickiMod(biljka)
                            }
                        }
                        else{
                            isBrzaPretragaActive = false
                            biljkeRV.adapter = BotanickiAdapter.BotanickiAdapter(this@MainActivity, biljke, baza) {
                                    biljka -> filterBotanickiMod(biljka) }
                        }
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                spinner.setSelection(layoutOptions.indexOf("Medicinski"))
            }
        }

        resetBtn.setOnClickListener {
            isBrzaPretragaActive=false
            filtriranaLista=null
            when (spinner.selectedItemPosition) {
                0 -> biljkeRV.adapter =
                    MedicinskiAdapter.MedicinskiAdapter(this@MainActivity, biljke, baza) { biljka -> filterMedicinskiMod(biljka) }
                1 -> biljkeRV.adapter =
                    KuharskiAdapter.KuharskiAdapter(this@MainActivity, biljke, baza) { biljka -> filterKuharskiMod(biljka) }
                2 -> biljkeRV.adapter =
                    BotanickiAdapter.BotanickiAdapter(this@MainActivity, biljke, baza) { biljka -> filterBotanickiMod(biljka) }
            }
        }

        //brzaPretraga Click
        brzaPretraga.setOnClickListener {
            val substr = pretragaET.text.toString()
            val flowerColor = bojaSPIN.selectedItem.toString()

            val trefleDAO = TrefleDAO()
            trefleDAO.setContext(this)

            if(substr.isNotBlank()){
                CoroutineScope(Dispatchers.Main).launch {
                    isBrzaPretragaActive=true
                    val pretrazeneBiljke = withContext(Dispatchers.IO) {
                        trefleDAO.getPlantsWithFlowerColor(flowerColor, substr)
                    }
                    filtriranaLista = pretrazeneBiljke

                    // Ažurirajte RecyclerView sa rezultatima brze pretrage
                    updateRecyclerView(filtriranaLista!!)
                }
            }

        }

        // Postavljanje Paddinga koristeći WindowInsets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dodajNovuBiljkuBtn: Button = findViewById(R.id.novaBiljkaBtn)
        dodajNovuBiljkuBtn.setOnClickListener {
            val intent = Intent(this, NovaBiljkaActivity::class.java)
            startActivityForResult(intent, NOVA_BILJKA_REQUEST_CODE)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val brojBiljaka = biljkaDAO.brojBiljaka()
            if(brojBiljaka == 0){
                biljkaDAO.insertBiljka(biljke)
            }
            filtriranaLista = biljkaDAO.getAllBiljkas().toMutableList()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NOVA_BILJKA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Ažuriranje liste u RecyclerView-u
                biljkeRV.adapter = MedicinskiAdapter.MedicinskiAdapter(this, biljke, baza) { biljka -> }
            }
        }
    }
    private fun filterMedicinskiMod(odabranaBiljka: Biljka) {
        filtriranaLista = biljke.filter { biljka ->
            biljka.medicinskeKoristi.any { it in odabranaBiljka.medicinskeKoristi }
        }
        biljkeRV.adapter = MedicinskiAdapter.MedicinskiAdapter(this, filtriranaLista!!, baza) { biljka -> }
    }
    private fun filterKuharskiMod(odabranaBiljka: Biljka) {
        filtriranaLista = biljke.filter { biljka ->
            biljka.jela.any { it in odabranaBiljka.jela } ||
            biljka.profilOkusa == odabranaBiljka.profilOkusa
        }
        biljkeRV.adapter = KuharskiAdapter.KuharskiAdapter(this, filtriranaLista!!, baza) { biljka -> }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun filterBotanickiMod(odabranaBiljka: Biljka) {
        filtriranaLista = biljke.filter { biljka ->
            biljka.porodica == odabranaBiljka.porodica &&
                    biljka.klimatskiTipovi.any { it in odabranaBiljka.klimatskiTipovi } &&
                    biljka.zemljisniTipovi.any { it in odabranaBiljka.zemljisniTipovi }
        }
        if(!isBrzaPretragaActive)
        biljkeRV.adapter = BotanickiAdapter.BotanickiAdapter(this, filtriranaLista!!, baza) { biljka -> }
    }

    // Spirala 3
    private fun updateRecyclerView(updateBiljke: List<Biljka>) {
        when (spinner.selectedItemPosition) {
            0 -> biljkeRV.adapter = MedicinskiAdapter.MedicinskiAdapter(this@MainActivity, updateBiljke, baza) { biljka -> filterMedicinskiMod(biljka) }
            1 -> biljkeRV.adapter = KuharskiAdapter.KuharskiAdapter(this@MainActivity, updateBiljke, baza) { biljka -> filterKuharskiMod(biljka) }
            2 -> biljkeRV.adapter = BotanickiAdapter.BotanickiAdapter(this@MainActivity, updateBiljke, baza) { biljka -> filterBotanickiMod(biljka) }
        }
    }
}
