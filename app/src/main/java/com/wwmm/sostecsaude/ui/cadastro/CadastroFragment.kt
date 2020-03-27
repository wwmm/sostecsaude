package com.wwmm.sostecsaude.ui.cadastro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar

import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.fragment_cadastro.*


class CadastroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_cadastro, container, false)

        val prefs = requireActivity().getSharedPreferences(
            "UserInfo",
            0
        )

        val name = prefs.getString("Name", "")!!
        val email = prefs.getString("Email", "")!!
        val consertar = prefs.getBoolean("InteresseRelatar", false)

        if (name.isNotBlank() || email.isNotBlank()) {
            val controller = Navigation.findNavController(
                requireActivity(),
                R.id.nav_host_main
            )

            controller.navigate(R.id.interessesFragment)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controller = Navigation.findNavController(
            requireActivity(),
            R.id.nav_host_main
        )

        button_entrar.setOnClickListener {
            var goAhead = checkBox_hospital.isChecked || checkBox_manutencao.isChecked
                    || checkBox_transporte.isChecked

            if(!goAhead){
                Snackbar.make(
                    main_layout_cadastro, "Escolha ao menos 1 interesse!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            if(goAhead && (editText_nome.text.isBlank() || editText_email.text.isBlank())){
                goAhead = false

                Snackbar.make(
                    main_layout_cadastro, "Insira seus dados!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            if(goAhead){
                val prefs = requireActivity().getSharedPreferences(
                    "UserInfo",
                    0
                )

                val editor = prefs.edit()

                editor.putString("Name", editText_nome.text.toString())
                editor.putString("Email", editText_email.text.toString())
                editor.putBoolean("InteresseRelatar", checkBox_hospital.isChecked)
                editor.putBoolean("InteresseConsertar", checkBox_manutencao.isChecked)
                editor.putBoolean("InteresseTransportar", checkBox_transporte.isChecked)

                editor.apply()

                if(!checkBox_manutencao.isChecked && !checkBox_transporte.isChecked){
//                    controller.navigate(R.id.relatarDanosFragment)
                }else {
                    controller.navigate(R.id.interessesFragment)
                }
            }
        }
    }
}
