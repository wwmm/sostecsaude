package com.wwmm.sostecsaude.ui.unidade_manutencao

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.wwmm.sostecsaude.R
import org.json.JSONArray
import java.util.*

class AdapterStatusReparo(private val fragment: StatusReparo, private val lines: JSONArray) :
    RecyclerView.Adapter<AdapterStatusReparo.ViewHolder>(), Filterable {
    private var mFilterArray = lines

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_unidade_manutencao_status_reparo, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val line = mFilterArray[position] as JSONObject

//        val nome = line.getString("Nome")
//        val fabricante = line.getString("Fabricante")
//        val modelo = line.getString("Modelo")
//        val numeroSerie = line.getString("NumeroSerie")
//        val quantidade = line.getString("Quantidade")
//        val defeito = line.getString("Defeito")
//
//        holder.view.textView_nome.text = nome
//        holder.view.textView_fabricante.text = fabricante
//        holder.view.textView_modelo.text = modelo
//        holder.view.textView_numero_serie.text = numeroSerie
//        holder.view.textView_defeito.text = defeito
//        holder.view.textView_quantidade.text = quantidade
    }

    override fun getItemCount() = mFilterArray.length()

    override fun getFilter(): Filter {
        return object : Filter() {
            private val results = FilterResults()

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                if (constraint.isNullOrBlank()) {
                    results.count = lines.length()
                    results.values = lines
                } else {
                    val filteredArray = JSONArray()

                    for (n in 0 until lines.length()) {
                        if (lines[n].toString().toLowerCase(Locale.ENGLISH).contains(constraint)) {
                            filteredArray.put(lines[n])
                        }
                    }

                    results.count = filteredArray.length()
                    results.values = filteredArray
                }

                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mFilterArray = results?.values as JSONArray

                notifyDataSetChanged()
            }
        }
    }

    companion object {
        const val LOGTAG = "AdapterStatusReparo"
    }
}