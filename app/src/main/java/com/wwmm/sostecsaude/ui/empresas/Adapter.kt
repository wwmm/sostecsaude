package com.wwmm.sostecsaude.ui.empresas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.wwmm.sostecsaude.Equipamentos
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.recyclerview_empresa_ver_pedidos_contents.view.*
import kotlinx.android.synthetic.main.recyclerview_relatar_danos_contents.view.*
import kotlinx.android.synthetic.main.recyclerview_relatar_danos_contents.view.editText_defeito
import kotlinx.android.synthetic.main.recyclerview_relatar_danos_contents.view.editText_equipamento
import kotlinx.android.synthetic.main.recyclerview_relatar_danos_contents.view.editText_fabricante
import kotlinx.android.synthetic.main.recyclerview_relatar_danos_contents.view.editText_modelo
import kotlinx.android.synthetic.main.recyclerview_relatar_danos_contents.view.editText_numero_serie
import kotlinx.android.synthetic.main.recyclerview_relatar_danos_contents.view.editText_quantidade
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class Adapter(private val lines: ArrayList<ResultRow>) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_empresa_ver_pedidos_contents, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val line = lines[position]

        val unidadeSaude = line[Equipamentos.unidade_saude]
        val local = line[Equipamentos.local]
        val equipamento = line[Equipamentos.equipamento]
        val fabricante = line[Equipamentos.fabricante]
        val modelo = line[Equipamentos.modelo]
        val numeroSerie = line[Equipamentos.numero_serie]
        val defeito = line[Equipamentos.defeito]
        val quantidade = line[Equipamentos.quantidade]

        holder.view.editText_unidade_saude.setText(unidadeSaude)
        holder.view.editText_local.setText(local)
        holder.view.editText_equipamento.setText(equipamento)
        holder.view.editText_fabricante.setText(fabricante)
        holder.view.editText_modelo.setText(modelo)
        holder.view.editText_numero_serie.setText(numeroSerie)
        holder.view.editText_defeito.setText(defeito)
        holder.view.editText_quantidade.setText(quantidade.toString())
    }

    override fun getItemCount() = lines.size
}