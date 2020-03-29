package com.wwmm.sostecsaude.ui.interesses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.fragment_interesses.*

class InteressesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_interesses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = requireActivity().findViewById(R.id.bottom_nav) as BottomNavigationView

        val controller = findNavController()

        button_pedir_reparo.setOnClickListener {
            bottomNav.menu.clear()

            bottomNav.inflateMenu(R.menu.menu_bottom_nav_relatar)

            bottomNav.visibility = View.VISIBLE

            bottomNav.setOnNavigationItemSelectedListener(null)

            bottomNav.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_bottomnav_relatar_danos_add -> {
                        controller.navigate(R.id.action_global_addFragment)
                    }

                    R.id.menu_bottomnav_relatar_danos_submissoes -> {
                        controller.navigate(R.id.action_global_listFragment)
                    }

                    R.id.menu_bottomnav_relatar_danos_oficinas -> {
                        controller.navigate(R.id.action_global_verOficinas)
                    }

                    R.id.menu_bottomnav_relatar_danos_contato -> {
                        controller.navigate(R.id.action_global_unidadeSaude)
                    }
                }

                true
            }

            controller.navigate(R.id.action_interessesFragment_to_nested_graph_relatar)
        }

        button_fazer_reparos.setOnClickListener {
            bottomNav.menu.clear()

            bottomNav.inflateMenu(R.menu.menu_bottom_nav_unidade_manutencao)

            bottomNav.visibility = View.VISIBLE

            bottomNav.setOnNavigationItemSelectedListener(null)

            bottomNav.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_bottomnav_unidade_manutencao_pedidos -> {
                        controller.navigate(R.id.action_global_empresasVerPedidos)
                    }

                    R.id.menu_bottomnav_unidade_manutencao_contato -> {
                        controller.navigate(R.id.action_global_unidadeManutencao)
                    }
                }

                true
            }

            controller.navigate(R.id.action_interessesFragment_to_nested_graph_unidade_manutencao)
        }

        controller.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.interessesFragment -> {
                    bottomNav.visibility = View.GONE
                }

                else -> {

                }
            }
        }
    }
}
