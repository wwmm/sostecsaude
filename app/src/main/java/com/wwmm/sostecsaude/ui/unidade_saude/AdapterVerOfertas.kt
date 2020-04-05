package com.wwmm.sostecsaude.ui.unidade_saude

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.recyclerview_unidade_saude_ver_ofertas.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class AdapterVerOfertas(private val lines: JSONArray) :
    RecyclerView.Adapter<AdapterVerOfertas.ViewHolder>(), Filterable {
    private var mFilterArray = lines

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_unidade_saude_ver_ofertas, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val line = mFilterArray[position] as JSONObject

        holder.view.textView_empresa.text = line.getString("Nome")
        holder.view.textView_setor.text = line.getString("Setor")
        holder.view.textView_cnpj.text = line.getString("CNPJ")
        holder.view.textView_local.text = line.getString("Local")
        holder.view.textView_telefone.text = line.getString("Telefone")
        holder.view.textView_email.text = line.getString("Email")
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
}