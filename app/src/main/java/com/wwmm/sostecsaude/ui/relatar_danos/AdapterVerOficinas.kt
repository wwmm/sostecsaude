package com.wwmm.sostecsaude.ui.relatar_danos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wwmm.sostecsaude.R
import org.json.JSONArray

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
        val line = lines[position]

//        val nome = line[Empresas.nome]
//        val setor = line[Empresas.setor]
//        val local = line[Empresas.local]
//        val contato = line[Empresas.contato]
//
//        holder.view.textView_empresa.text = nome
//        holder.view.textView_setor.text = setor
//        holder.view.textView_local.text = local
//        holder.view.textView_contato.text = contato
    }

    override fun getItemCount() = lines.length()
}