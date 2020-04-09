package com.wwmm.sostecsaude.ui.administration

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wwmm.sostecsaude.ui.administration.unidades_saude.ListaUnidades

class Adapter (fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ListaUnidades()
            }

            1 -> {
                ListaUnidades()
            }

            else -> {
                ListaUnidades()
            }
        }
    }
}