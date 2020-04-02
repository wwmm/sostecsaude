package com.wwmm.sostecsaude.ui.relatar_danos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.recyclerview_relatar_danos.view.*
import org.json.JSONArray
import org.json.JSONObject

class Adapter(private val lines: JSONArray, private val progressBar: ProgressBar) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_relatar_danos, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val line = lines[position] as JSONObject

        val id = line.getString("Id")
        val nome = line.getString("Nome")
        val fabricante = line.getString("Fabricante")
        val modelo = line.getString("Modelo")
        val numeroSerie = line.getString("NumeroSerie")
        val quantidade = line.getString("Quantidade")
        val defeito = line.getString("Defeito")

        holder.view.editText_nome.setText(nome)
        holder.view.editText_fabricante.setText(fabricante)
        holder.view.editText_modelo.setText(modelo)
        holder.view.editText_numero_serie.setText(numeroSerie)
        holder.view.editText_defeito.setText(defeito)
        holder.view.editText_quantidade.setText(quantidade)

//        holder.view.button_remove.setOnClickListener {
//            progressBar.visibility = View.VISIBLE
//
//            GlobalScope.launch(Dispatchers.IO) {
//                transaction {
//                    Equipamentos.deleteWhere {
//                        Equipamentos.id eq id
//                    }
//                }
//
//                GlobalScope.launch(Dispatchers.Main) {
//                    progressBar.visibility = View.GONE
//
//                    lines.remove(line)
//
//                    notifyItemRemoved(position)
//                }
//            }
//        }

        holder.view.button_update.setOnClickListener {
//            equipamento = holder.view.editText_nome.text.toString()
//            fabricante = holder.view.editText_fabricante.text.toString()
//            modelo = holder.view.editText_modelo.text.toString()
//            numeroSerie = holder.view.editText_numero_serie.text.toString()
//            defeito = holder.view.editText_defeito.text.toString()
//            quantidade = holder.view.editText_quantidade.text.toString().toInt()
//
//            progressBar.visibility = View.VISIBLE
//
//            GlobalScope.launch(Dispatchers.IO) {
//                transaction {
//                    Equipamentos.update({ Equipamentos.id eq id }) {
//                        it[Equipamentos.equipamento] = equipamento
//                        it[Equipamentos.fabricante] = fabricante
//                        it[Equipamentos.modelo] = modelo
//                        it[Equipamentos.numero_serie] = numeroSerie
//                        it[Equipamentos.defeito] = defeito
//                        it[Equipamentos.quantidade] = quantidade
//                    }
//                }
//
//                GlobalScope.launch(Dispatchers.Main) { progressBar.visibility = View.GONE }
//            }
        }
    }

    override fun getItemCount() = lines.length()
}