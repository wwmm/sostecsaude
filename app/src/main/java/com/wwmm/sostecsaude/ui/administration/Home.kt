package com.wwmm.sostecsaude.ui.administration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager

import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.fragment_administration_home.*

class Home : Fragment(), Toolbar.OnMenuItemClickListener {
    private lateinit var mController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_administration_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mController = findNavController()

        toolbar.setupWithNavController(mController)

        toolbar.inflateMenu(R.menu.menu_toolbar)
        toolbar.menu.findItem(R.id.menu_search).isVisible = false
        toolbar.menu.findItem(R.id.menu_atualizar_perfil).isVisible = false
        toolbar.setOnMenuItemClickListener(this)

        button_gerenciar_usuarios.setOnClickListener {

        }

        button_unidade_saude.setOnClickListener {

        }

        button_unidade_manutencao.setOnClickListener {

        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_login -> {
                val prefs =
                    PreferenceManager.getDefaultSharedPreferences(requireContext())

                val editor = prefs.edit()

                editor.putString("Token", "")
                editor.putString("Perfil", "")
                editor.putString("Email", "")

                editor.apply()

                mController.navigate(R.id.action_global_fazerLogin)

                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}
