package com.wwmm.sostecsaude.ui.unidade_saude

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.wwmm.sostecsaude.R
import com.wwmm.sostecsaude.myServerURL
import kotlinx.android.synthetic.main.fragment_empresas_ver_pedidos.*
import kotlinx.android.synthetic.main.recyclerview_unidade_saude_registrar.view.*
import org.json.JSONArray
import org.json.JSONObject

class Adapter(private val frag: Fragment, private val lines: JSONArray) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_unidade_saude_registrar, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val line = lines[position] as JSONObject

        val id = line.getString("ID")
        var nome = line.getString("Nome")
        var fabricante = line.getString("Fabricante")
        var modelo = line.getString("Modelo")
        var numeroSerie = line.getString("NumeroSerie")
        var quantidade = line.getString("Quantidade")
        var defeito = line.getString("Defeito")

        holder.view.editText_nome.setText(nome)
        holder.view.editText_fabricante.setText(fabricante)
        holder.view.editText_modelo.setText(modelo)
        holder.view.editText_numero_serie.setText(numeroSerie)
        holder.view.editText_defeito.setText(defeito)
        holder.view.editText_quantidade.setText(quantidade)

        val prefs = frag.requireActivity().getSharedPreferences(
            "UserInfo",
            0
        )

        val token = prefs.getString("Token", "")!!

        val queue = Volley.newRequestQueue(frag.requireContext())

        holder.view.button_remove.setOnClickListener {
            frag.progressBar.visibility = View.VISIBLE

            val request = object : StringRequest(
                Method.POST, "$myServerURL/unidade_saude_remover_equipamento",
                Response.Listener { response ->
                    val msg = response.toString()

                    if (msg == "invalid_token") {
                        val controller = frag.findNavController()

                        controller.navigate(R.id.action_verPedidos_to_fazerLogin)
                    } else {
                        lines.remove(position)

                        notifyItemRemoved(position)

                        frag.progressBar.visibility = View.GONE
                    }
                },
                Response.ErrorListener {
                    Log.d(LOGTAG, "failed request: $it")
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val parameters = HashMap<String, String>()

                    parameters["token"] = token
                    parameters["id"] = id

                    return parameters
                }
            }

            queue.add(request)
        }

        holder.view.button_update.setOnClickListener {
            nome = holder.view.editText_nome.text.toString()
            fabricante = holder.view.editText_fabricante.text.toString()
            modelo = holder.view.editText_modelo.text.toString()
            numeroSerie = holder.view.editText_numero_serie.text.toString()
            defeito = holder.view.editText_defeito.text.toString()
            quantidade = holder.view.editText_quantidade.text.toString()

            frag.progressBar.visibility = View.VISIBLE

            val request = object : StringRequest(
                Method.POST, "$myServerURL/unidade_saude_atualizar_equipamento",
                Response.Listener { response ->
                    val msg = response.toString()

                    if (msg == "invalid_token") {
                        val controller = frag.findNavController()

                        controller.navigate(R.id.action_verPedidos_to_fazerLogin)
                    } else {
                        frag.progressBar.visibility = View.GONE
                    }
                },
                Response.ErrorListener {
                    Log.d(LOGTAG, "failed request: $it")
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val parameters = HashMap<String, String>()

                    parameters["token"] = token
                    parameters["id"] = id
                    parameters["nome"] = nome
                    parameters["fabricante"] = fabricante
                    parameters["modelo"] = modelo
                    parameters["numero_serie"] = numeroSerie
                    parameters["quantidade"] = quantidade
                    parameters["defeito"] = defeito

                    return parameters
                }
            }

            queue.add(request)
        }
    }

    override fun getItemCount() = lines.length()

    companion object {
        const val LOGTAG = "adapter ver pedidos"
    }
}