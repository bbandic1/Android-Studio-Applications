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

class KuharskiAdapter {

    class KuharskiAdapter(private val context: Context, private var recepti: List<Biljka>,
                          private val baza: BiljkaDatabase, private val onItemClick: (biljka: Biljka) -> Unit) :
        RecyclerView.Adapter<KuharskiAdapter.KuharskiViewHolder>() {

        private lateinit var biljkaDAO: BiljkaDAO
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KuharskiViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.kuharski, parent, false)
            return KuharskiViewHolder(view)
        }

        override fun onBindViewHolder(holder: KuharskiViewHolder, position: Int) {
            val biljka = recepti[position]

            holder.naziv.text = biljka.naziv
            holder.slika.setImageResource(R.drawable.biljka)
            holder.profilOkusa.text = biljka.profilOkusa.opis

            if (biljka.jela.size >= 1) {
                holder.jelo1.text = biljka.jela[0]
            }
            if(biljka.jela.size<1){
                holder.jelo1.text=""
            }
            if (biljka.jela.size >= 2) {
                holder.jelo2.text = biljka.jela[1]
            }
            if(biljka.jela.size<2){
                holder.jelo2.text=""
            }
            if (biljka.jela.size >= 3) {
                holder.jelo3.text = biljka.jela[2]
            }
            if(biljka.jela.size<3){
                holder.jelo3.text=""
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
            return recepti.size
        }

        inner class KuharskiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val naziv: TextView = itemView.findViewById(R.id.nazivItem)
            val slika: ImageView = itemView.findViewById(R.id.slikaItem)
            val profilOkusa: TextView = itemView.findViewById(R.id.profilOkusaItem)
            val jelo1: TextView = itemView.findViewById(R.id.jelo1Item)
            val jelo2: TextView = itemView.findViewById(R.id.jelo2Item)
            val jelo3: TextView = itemView.findViewById(R.id.jelo3Item)
        }
    }

}