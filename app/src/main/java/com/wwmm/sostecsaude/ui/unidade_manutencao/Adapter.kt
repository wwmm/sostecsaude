package com.wwmm.sostecsaude.ui.unidade_manutencao

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.wwmm.sostecsaude.R
import com.wwmm.sostecsaude.myServerURL
import kotlinx.android.synthetic.main.fragment_unidade_manutencao.*
import kotlinx.android.synthetic.main.recyclerview_unidade_manutencao_ver_pedidos.*
import kotlinx.android.synthetic.main.recyclerview_unidade_manutencao_ver_pedidos.view.*
import org.json.JSONArray
import org.json.JSONObject

class ViewPager2Adapter(fragment: Fragment, private val line: JSONObject) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                val fragment = TabEquipamento()

                fragment.arguments = Bundle().apply {
                    putString("Nome", line.getString("Nome"))
                    putString("Fabricante", line.getString("Fabricante"))
                    putString("Modelo", line.getString("Modelo"))
                    putString("NumeroSerie", line.getString("NumeroSerie"))
                }

                return fragment
            }

            1 -> {
                val fragment = TabDefeito()

                fragment.arguments = Bundle().apply {
                    putString("Defeito", line.getString("Defeito"))
                    putString("Quantidade", line.getInt("Quantidade").toString())
                }

                return fragment
            }

            else -> {
                val fragment = TabUnidadeSaude()

                fragment.arguments = Bundle().apply {
                    putString("Nome", line.getString("Unidade"))
                    putString("Local", line.getString("Local"))
                }

                return fragment
            }
        }
    }
}

class Adapter(
    private val fragment: Fragment, private val equipamentos: JSONArray,
    private val idNumbers: JSONArray
) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_unidade_manutencao_ver_pedidos, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val line = equipamentos[position] as JSONObject

        val id = line.getString("ID")

        holder.view.viewpager.adapter = ViewPager2Adapter(fragment, line)

        TabLayoutMediator(holder.view.tab_layout, holder.view.viewpager) { tab, tabIdx ->
            when (tabIdx) {
                0 -> {
                    tab.text = fragment.getString(R.string.title_equipamento)
                    tab.setIcon(R.drawable.ic_scanner)
                }

                1 -> {
                    tab.text = fragment.getString(R.string.title_defeito)
                    tab.setIcon(R.drawable.ic_broken_image)
                }

                2 -> {
                    tab.text = fragment.getString(R.string.title_local)
                    tab.setIcon(R.drawable.ic_home_black_24dp)
                }
            }
        }.attach()

        for (n in 0 until idNumbers.length()) {
            if (idNumbers[n] == id) {
                holder.view.switch_consertar.isChecked = true

                break
            }
        }

        val prefs = fragment.requireActivity().getSharedPreferences(
            "UserInfo",
            0
        )

        val token = prefs.getString("Token", "")!!

        val queue = Volley.newRequestQueue(fragment.requireContext())

        holder.view.switch_consertar.setOnCheckedChangeListener { _, state ->
            fragment.progressBar.visibility = View.VISIBLE

            val request = object : StringRequest(
                Method.POST, "$myServerURL/unidade_manutencao_atualizar_interesse",
                Response.Listener { response ->
                    val msg = response.toString()

                    if (msg == "invalid_token") {
                        val controller = fragment.findNavController()
                        controller.navigate(R.id.action_unidadeManutencao_to_fazerLogin)
                    } else {
                        fragment.progressBar.visibility = View.GONE

                        Snackbar.make(
                            fragment.layout_manutencao_pedidos, msg,
                            Snackbar.LENGTH_SHORT
                        ).show()
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
                    parameters["state"] = state.toString()

                    return parameters
                }
            }

            queue.add(request)
        }
    }

    override fun getItemCount() = equipamentos.length()

    companion object {
        const val LOGTAG = "manutencao ver pedidos"
    }
}