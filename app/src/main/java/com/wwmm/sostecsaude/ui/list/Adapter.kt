package com.wwmm.sostecsaude.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wwmm.sostecsaude.Equipamentos
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.recyclerview_contents.view.*
import org.jetbrains.exposed.sql.ResultRow

class Adapter(private val lines: ArrayList<ResultRow>) : RecyclerView.Adapter<Adapter.ViewHolder>() {
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
//        var queryStr = "select ${SQLHelper.DB_LOCAL},${SQLHelper.DB_EQUIPAMENTO},"
//
//        queryStr += "${SQLHelper.DB_DEFEITO},${SQLHelper.DB_QUANTIDADE} from "
//        queryStr += "${SQLHelper.DB_TABLE_NAME} where ${SQLHelper.DB_ID} = ?"
//
        val rowIdx = position + 1
//
//        val cursor = db.rawQuery(queryStr, arrayOf(rowIdx.toString()))

        val line= lines[position]

        var local = line[Equipamentos.local]
        var equipamento = line[Equipamentos.equipamento]
        var defeito = line[Equipamentos.defeito]
        var quantidade = line[Equipamentos.quantidade]

//        remoteServer.getLineWhereId(rowIdx)

//        if (cursor.count > 0) {
//            cursor.moveToFirst()
//
//            local = cursor.getString(cursor.getColumnIndex(SQLHelper.DB_LOCAL))
//            equipamento = cursor.getString(cursor.getColumnIndex(SQLHelper.DB_EQUIPAMENTO))
//            defeito = cursor.getString(cursor.getColumnIndex(SQLHelper.DB_DEFEITO))
//            quantidade = cursor.getInt(cursor.getColumnIndex(SQLHelper.DB_QUANTIDADE))
//        }

        holder.view.editText_local.setText(local)
        holder.view.editText_equipamento.setText(equipamento)
        holder.view.editText_defeito.setText(defeito)
        holder.view.editText_quantidade.setText(quantidade.toString())

//        holder.view.button_remove.setOnClickListener {
//            db.deleteSelection(rowIdx)
//
//            notifyItemRemoved(position)
//        }

//        holder.view.button_update.setOnClickListener {
//            local = holder.view.editText_local.text.toString()
//            equipamento = holder.view.editText_equipamento.text.toString()
//            defeito = holder.view.editText_defeito.text.toString()
//            quantidade = holder.view.editText_quantidade.text.toString().toInt()
//
//            db.update(rowIdx, local, equipamento, defeito, quantidade)
//        }
    }

    override fun getItemCount() = lines.size
}