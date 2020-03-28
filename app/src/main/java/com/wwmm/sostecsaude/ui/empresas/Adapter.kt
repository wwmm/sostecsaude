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

class ViewPager2Adapter(fragment: Fragment, private val line: ResultRow) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                val fragment = TabEquipamento()

                fragment.arguments = Bundle().apply {
                    putString("Equipamento", line[Equipamentos.equipamento])
                    putString("Fabricante", line[Equipamentos.fabricante])
                    putString("Modelo", line[Equipamentos.modelo])
                    putString("NumeroSerie", line[Equipamentos.numero_serie])
                }

                return fragment
            }

            1 -> {
                val fragment = TabDefeito()

                fragment.arguments = Bundle().apply {
                    putString("Defeito", line[Equipamentos.defeito])
                    putString("Quantidade", line[Equipamentos.quantidade].toString())
                }

                return fragment
            }

            else -> {
                val fragment = TabUnidadeSaude()

                fragment.arguments = Bundle().apply {
                    putString("Nome", line[Equipamentos.unidade_saude])
                    putString("Local", line[Equipamentos.local])
                }

                return fragment
            }
        }
    }
}

class Adapter(private val fragment: Fragment, private val lines: ArrayList<ResultRow>) :
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

        holder.view.viewpager.adapter = ViewPager2Adapter(fragment, line)

        TabLayoutMediator(holder.view.tab_layout, holder.view.viewpager) { tab, tabIdx ->
            when(tabIdx){
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