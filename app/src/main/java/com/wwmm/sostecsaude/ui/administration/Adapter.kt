package com.wwmm.sostecsaude.ui.administration

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wwmm.sostecsaude.ui.administration.equipamentos.ListaEquipamentos
import com.wwmm.sostecsaude.ui.administration.unidades_manutencao.ListaUnidadeManutencao
import com.wwmm.sostecsaude.ui.administration.unidades_saude.ListaUnidadeSaude

class Adapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ListaUnidadeSaude()
            }

            1 -> {
                ListaUnidadeManutencao()
            }

            2 -> {
                ListaEquipamentos()
            }

            else -> {
                ListaUnidadeSaude()
            }
        }
    }
}