package com.wwmm.sostecsaude.ui.unidade_saude

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.recyclerview_relatar_danos_ver_oficinas.view.*
import org.json.JSONArray
import org.json.JSONObject

class AdapterVerOficinas(private val lines: JSONArray) :
    RecyclerView.Adapter<AdapterVerOficinas.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_relatar_danos_ver_oficinas, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val line = lines[position] as JSONObject

        holder.view.textView_empresa.text = line.getString("Nome")
        holder.view.textView_setor.text = line.getString("Setor")
        holder.view.textView_cnpj.text = line.getString("CNPJ")
        holder.view.textView_local.text = line.getString("Local")
        holder.view.textView_contato.text = line.getString("Email")
    }

    override fun getItemCount() = lines.length()
}