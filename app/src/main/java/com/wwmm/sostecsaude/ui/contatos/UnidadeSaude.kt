package com.wwmm.sostecsaude.ui.contatos

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.fragment_contatos_unidadesaude.*


class UnidadeSaude : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contatos_unidadesaude, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controller = findNavController()

        val prefs = requireActivity().getSharedPreferences(
            "UserInfo",
            0
        )

        val name = prefs.getString("Name", "")
        val email = prefs.getString("Email", "")

        editText_nome.setText(name)
        editText_email.setText(email)

        button_save.setOnClickListener {
            val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                    InputMethodManager?

            imm?.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)

            if (editText_nome.text.isNotBlank() && editText_email.text.isNotBlank()) {
                val editor = prefs.edit()

                editor.putString("Name", editText_nome.text.toString())
                editor.putString("Email", editText_email.text.toString())

                editor.apply()

                Snackbar.make(
                    main_layout_contato, "Dados Salvos!",
                    Snackbar.LENGTH_SHORT
                ).show()

                controller.navigate(R.id.action_unidadeSaude_to_addFragment)
            } else {
                Snackbar.make(
                    main_layout_contato, "Preencha todos os campos!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}
