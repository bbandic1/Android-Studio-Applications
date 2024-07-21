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


class MedicinskiAdapter {

    class MedicinskiAdapter(private val context: Context, private var biljke: List<Biljka>,
                            private val baza: BiljkaDatabase,
                            private val onItemClick: (biljka: Biljka) -> Unit) :
        RecyclerView.Adapter<MedicinskiAdapter.MedicinskiViewHolder>() {

        private lateinit var biljkaDAO: BiljkaDAO
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicinskiViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.medicinski, parent, false)
            return MedicinskiViewHolder(view)
        }

        override fun onBindViewHolder(holder: MedicinskiViewHolder, position: Int) {
            val biljka = biljke[position]

            holder.naziv.text = biljka.naziv

            holder.slika.setImageResource(R.drawable.biljka)

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

            val medicinskeKoristi = biljka.medicinskeKoristi
            if (medicinskeKoristi.size >= 1) {
                holder.korist1.text = medicinskeKoristi[0].opis
            }
            if(medicinskeKoristi.size < 1){
                holder.korist1.text="";
            }
            if (medicinskeKoristi.size >= 2) {
                holder.korist2.text = medicinskeKoristi[1].opis
            }
            if(medicinskeKoristi.size < 2){
                holder.korist2.text="";
            }
            if (medicinskeKoristi.size >= 3) {
                holder.korist3.text = medicinskeKoristi[2].opis
            }
            if(medicinskeKoristi.size < 3){
                holder.korist3.text="";
            }

            holder.upozorenje.text = biljka.medicinskoUpozorenje

            holder.itemView.setOnClickListener {
                onItemClick(biljka)
            }
        }
        override fun getItemCount(): Int {
            return biljke.size
        }
        inner class MedicinskiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val naziv: TextView = itemView.findViewById(R.id.nazivItem)
            val slika: ImageView = itemView.findViewById(R.id.slikaItem)
            val korist1: TextView = itemView.findViewById(R.id.korist1Item)
            val korist2: TextView = itemView.findViewById(R.id.korist2Item)
            val korist3: TextView = itemView.findViewById(R.id.korist3Item)
            val upozorenje: TextView = itemView.findViewById(R.id.upozorenjeItem)
        }
    }

}