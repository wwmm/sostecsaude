package com.wwmm.sostecsaude.ui.empresas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.recyclerview_empresa_ver_pedidos_contents.view.*
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

class Adapter(private val fragment: Fragment, private val lines: JSONArray) :
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
        val line = lines[position] as JSONObject

        holder.view.viewpager.adapter = ViewPager2Adapter(fragment, line)

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

    override fun getItemCount() = lines.length()
}