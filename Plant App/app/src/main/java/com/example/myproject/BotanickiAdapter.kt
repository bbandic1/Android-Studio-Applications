package com.example.myproject

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BotanickiAdapter {

    class BotanickiAdapter(private val context: Context, private var biljke: List<Biljka>,
                           private val baza: BiljkaDatabase, private val onItemClick: (biljka: Biljka) -> Unit) :
        RecyclerView.Adapter<BotanickiAdapter.BotanickiViewHolder>() {

        private lateinit var biljkaDAO: BiljkaDAO

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BotanickiViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.botanicki, parent, false)
            return BotanickiViewHolder(view)
        }

        override fun onBindViewHolder(holder: BotanickiViewHolder, position: Int) {
            val biljka = biljke[position]

            holder.naziv.text = biljka.naziv
            holder.slika.setImageResource(R.drawable.biljka)
            holder.porodica.text = biljka.porodica

            if (biljka.zemljisniTipovi.size >= 1) {
                holder.zemljisniTip.text = biljka.zemljisniTipovi[0].toString()
            }
            if (biljka.klimatskiTipovi.size >= 1) {
                holder.klimatskiTip.text = biljka.klimatskiTipovi[0].opis
            }
            holder.itemView.setOnClickListener {
                onItemClick(biljka)
            }

            // Inicijalizacija biljkaDAO
            biljkaDAO = baza.biljkaDao()

            val trefleDAO = TrefleDAO()
            trefleDAO.setContext(context)

            CoroutineScope(Dispatchers.Main).launch {
                val bitmap = trefleDAO.getImage(biljka)
                if (bitmap != null) {
                    var provjera = 0
                    val lista = biljkaDAO.getIDBiljke()
                    for (instanca in lista) {
                        Log.d("ProvjeraPetlje", "Instanca: $instanca, Biljka ID: ${biljka.id}, provjera: ${provjera}")
                        if (instanca == biljka.id) {
                            provjera = 1
                            break
                        }
                    }
                    if (provjera == 0) {
                        biljkaDAO.addImage(biljka.id, bitmap)
                    }
                }
                withContext(Dispatchers.Main) {
                    holder.slika.setImageBitmap(bitmap)
                }
            }
        }

        override fun getItemCount(): Int {
            return biljke.size
        }

        inner class BotanickiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val naziv: TextView = itemView.findViewById(R.id.nazivItem)
            val slika: ImageView = itemView.findViewById(R.id.slikaItem)
            val porodica: TextView = itemView.findViewById(R.id.porodicaItem)
            val zemljisniTip: TextView = itemView.findViewById(R.id.zemljisniTipItem)
            val klimatskiTip: TextView = itemView.findViewById(R.id.klimatskiTipItem)
        }
    }
}