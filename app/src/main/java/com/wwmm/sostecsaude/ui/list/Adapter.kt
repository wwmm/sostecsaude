package com.wwmm.sostecsaude.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.wwmm.sostecsaude.Equipamentos
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.recyclerview_contents.view.*
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
            .inflate(R.layout.recyclerview_contents, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val line = lines[position]

        val id = line[Equipamentos.id]
        var local = line[Equipamentos.local]
        var equipamento = line[Equipamentos.equipamento]
        var defeito = line[Equipamentos.defeito]
        var quantidade = line[Equipamentos.quantidade]

        holder.view.editText_local.setText(local)
        holder.view.editText_equipamento.setText(equipamento)
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
            local = holder.view.editText_local.text.toString()
            equipamento = holder.view.editText_equipamento.text.toString()
            defeito = holder.view.editText_defeito.text.toString()
            quantidade = holder.view.editText_quantidade.text.toString().toInt()

            progressBar.visibility = View.VISIBLE

            GlobalScope.launch(Dispatchers.IO) {
                transaction {
                    Equipamentos.update({ Equipamentos.id eq id }) {
                        it[Equipamentos.local] = local
                        it[Equipamentos.equipamento] = equipamento
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