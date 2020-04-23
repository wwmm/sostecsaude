package com.wwmm.sostecsaude.ui.unidade_manutencao

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wwmm.sostecsaude.ui.administration.equipamentos.ListaEquipamentos
import com.wwmm.sostecsaude.ui.administration.unidades_manutencao.ListaUnidadeManutencao
import com.wwmm.sostecsaude.ui.administration.unidades_saude.ListaUnidadeSaude

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                VerPedidos()
            }

            1 -> {
                StatusReparo()
            }

            2 ->{
                Relatorio()
            }

            else -> {
                VerPedidos()
            }
        }
    }
}