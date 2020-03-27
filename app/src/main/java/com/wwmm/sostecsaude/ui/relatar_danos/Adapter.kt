package com.wwmm.sostecsaude.ui.relatar_danos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.wwmm.sostecsaude.Equipamentos
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.recyclerview_relatar_danos_contents.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class Adapter(private val lines: ArrayList<ResultRow>, private val progressBar: ProgressBar) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_relatar_danos_contents, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val line = lines[position]

        val id = line[Equipamentos.id]
        var equipamento = line[Equipamentos.equipamento]
        var fabricante = line[Equipamentos.fabricante]
        var modelo = line[Equipamentos.modelo]
        var numeroSerie = line[Equipamentos.numero_serie]
        var defeito = line[Equipamentos.defeito]
        var quantidade = line[Equipamentos.quantidade]

        holder.view.editText_equipamento.setText(equipamento)
        holder.view.editText_fabricante.setText(fabricante)
        holder.view.editText_modelo.setText(modelo)
        holder.view.editText_numero_serie.setText(numeroSerie)
        holder.view.editText_defeito.setText(defeito)
        holder.view.editText_quantidade.setText(quantidade.toString())

        holder.view.button_remove.setOnClickListener {
            progressBar.visibility = View.VISIBLE

            GlobalScope.launch(Dispatchers.IO) {
                transaction {
                    Equipamentos.deleteWhere {
                        Equipamentos.id eq id
                    }
                }

                GlobalScope.launch(Dispatchers.Main) {
                    progressBar.visibility = View.GONE

                    lines.remove(line)

                    notifyItemRemoved(position)
                }
            }
        }

        holder.view.button_update.setOnClickListener {
            equipamento = holder.view.editText_equipamento.text.toString()
            fabricante = holder.view.editText_fabricante.text.toString()
            modelo = holder.view.editText_modelo.text.toString()
            numeroSerie = holder.view.editText_numero_serie.text.toString()
            defeito = holder.view.editText_defeito.text.toString()
            quantidade = holder.view.editText_quantidade.text.toString().toInt()

            progressBar.visibility = View.VISIBLE

            GlobalScope.launch(Dispatchers.IO) {
                transaction {
                    Equipamentos.update({ Equipamentos.id eq id }) {
                        it[Equipamentos.equipamento] = equipamento
                        it[Equipamentos.fabricante] = fabricante
                        it[Equipamentos.modelo] = modelo
                        it[Equipamentos.numero_serie] = numeroSerie
                        it[Equipamentos.defeito] = defeito
                        it[Equipamentos.quantidade] = quantidade
                    }
                }

                GlobalScope.launch(Dispatchers.Main) { progressBar.visibility = View.GONE }
            }
        }
    }

    override fun getItemCount() = lines.size
}