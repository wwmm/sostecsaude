package com.wwmm.sostecsaude.ui.empresas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.wwmm.sostecsaude.Equipamentos
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.recyclerview_empresa_ver_pedidos_contents.view.*
import org.jetbrains.exposed.sql.ResultRow

class ViewPager2Adapter(fragment: Fragment, private val line: Map<String, String>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                val fragment = TabEquipamento()

                fragment.arguments = Bundle().apply {
                    putString("Nome", line["nome"])
                    putString("Fabricante", line["fabricante"])
                    putString("Modelo", line["modelo"])
                    putString("NumeroSerie", line["numeroSerie"])
                }

                return fragment
            }

            1 -> {
                val fragment = TabDefeito()

                fragment.arguments = Bundle().apply {
                    putString("Defeito", line["defeito"])
                    putString("Quantidade", line["quantidade"].toString())
                }

                return fragment
            }

            else -> {
                val fragment = TabUnidadeSaude()

                fragment.arguments = Bundle().apply {
                    putString("Nome", line["unidade"])
                    putString("Local", line["local"])
                }

                return fragment
            }
        }
    }
}

class Adapter(private val fragment: Fragment, private val lines: List<String>) :
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

        val arr = line.split(":")

        if (arr.size == 9) {
            val id = arr[0]
            val nome = arr[1]
            val fabricante = arr[2]
            val modelo = arr[3]
            val numeroSerie = arr[4]
            val quantidade = arr[5]
            val defeito = arr[6]
            val unidade = arr[7]
            val local = arr[8]

            val dict = mapOf(
                "id" to id,
                "nome" to nome,
                "fabricante" to fabricante,
                "modelo" to modelo,
                "numeroSerie" to numeroSerie,
                "quantidade" to quantidade,
                "defeito" to defeito,
                "unidade" to unidade,
                "local" to local
            )

            holder.view.viewpager.adapter = ViewPager2Adapter(fragment, dict)
        }

        TabLayoutMediator(holder.view.tab_layout, holder.view.viewpager) { tab, tabIdx ->
            when (tabIdx) {
                0 -> {
                    tab.text = fragment.getString(R.string.title_equipamento)
                    tab.setIcon(R.drawable.ic_scanner)
                }

                1 -> {
                    tab.text = fragment.getString(R.string.title_defeito)
                    tab.setIcon(R.drawable.ic_build)
                }

                2 -> {
                    tab.text = fragment.getString(R.string.title_local)
                    tab.setIcon(R.drawable.ic_home_black_24dp)
                }
            }
        }.attach()
    }

    override fun getItemCount() = lines.size
}