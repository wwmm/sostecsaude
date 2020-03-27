package com.wwmm.sostecsaude.ui.contatos

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.fragment_contatos_unidade_saude.*


class UnidadeSaude : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contatos_unidade_saude, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity().getSharedPreferences(
            "UnidadeSaude",
            0
        )

        val unidadeSaude = prefs.getString("Unidade", "")
        val local = prefs.getString("Local", "")
        val name = prefs.getString("Name", "")
        val email = prefs.getString("Email", "")

        editText_unidade_saude.setText(unidadeSaude)
        editText_local.setText(local)
        editText_nome.setText(name)
        editText_email.setText(email)

        button_save.setOnClickListener {
            val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                    InputMethodManager?

            imm?.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)

            if (editText_nome.text.isNotBlank() && editText_email.text.isNotBlank()) {
                val editor = prefs.edit()

                editor.putString("Unidade", editText_unidade_saude.text.toString())
                editor.putString("Local", editText_local.text.toString())
                editor.putString("Name", editText_nome.text.toString())
                editor.putString("Email", editText_email.text.toString())

                editor.apply()

                Snackbar.make(
                    main_layout_contato, "Dados Salvos!",
                    Snackbar.LENGTH_SHORT
                ).show()

                val bottomNav = requireActivity().findViewById(R.id.bottom_nav) as
                        BottomNavigationView

                bottomNav.selectedItemId = R.id.menu_bottomnav_relatar_danos_add
            } else {
                Snackbar.make(
                    main_layout_contato, "Preencha todos os campos!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}
