package com.wwmm.sostecsaude.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wwmm.sostecsaude.R


class CarregarPerfil : Fragment() {
    private lateinit var mController: NavController
    private lateinit var mBottomNav: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_carregar_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mController = findNavController()

        mBottomNav = requireActivity().findViewById(R.id.bottom_nav) as BottomNavigationView

        mBottomNav.menu.clear()

        val prefs = requireActivity().getSharedPreferences(
            "UserInfo",
            0
        )

        when (prefs.getString("Perfil", "")) {
            "unidade_saude" -> {
                loadUnidadeSaude()
            }

            "unidade_manutencao" -> {
                loadUnidadeManutencao()
            }
        }
    }

    private fun loadUnidadeSaude() {
        mBottomNav.inflateMenu(R.menu.menu_bottom_nav_relatar)

        mBottomNav.visibility = View.VISIBLE

        mBottomNav.setOnNavigationItemSelectedListener(null)

        mBottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_bottomnav_relatar_danos_add -> {
                    mController.navigate(R.id.action_global_addFragment)
                }

                R.id.menu_bottomnav_relatar_danos_submissoes -> {
                    mController.navigate(R.id.action_global_listFragment)
                }

                R.id.menu_bottomnav_relatar_danos_oficinas -> {
                    mController.navigate(R.id.action_global_verOficinas)
                }
            }

            true
        }

        mController.navigate(R.id.action_carregarPerfil_to_nested_graph_relatar)
    }

    private fun loadUnidadeManutencao() {
//            mBottomNav.inflateMenu(R.menu.menu_bottom_nav_unidade_manutencao)
//
//            mBottomNav.visibility = View.VISIBLE
//
//            mBottomNav.setOnNavigationItemSelectedListener(null)
//
//            mBottomNav.setOnNavigationItemSelectedListener {
//                when (it.itemId) {
//                    R.id.menu_bottomnav_unidade_manutencao_pedidos -> {
//                        mController.navigate(R.id.action_global_empresasVerPedidos)
//                    }
//                }
//
//                true
//            }

        mController.navigate(R.id.action_carregarPerfil_to_nested_graph_unidade_manutencao)
    }

}
