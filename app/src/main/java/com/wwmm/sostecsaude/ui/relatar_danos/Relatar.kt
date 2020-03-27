package com.wwmm.sostecsaude.ui.relatar_danos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.wwmm.sostecsaude.Equipamentos
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.fragment_relatar_danos_relatar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.transactions.transaction

class Relatar : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_relatar_danos_relatar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controller = findNavController()

        if(!hasUserInfo()){
            controller.navigate(R.id.action_global_unidadeSaude)
        }else {
            progressBar.visibility = View.GONE

            button_add.setOnClickListener {
                val equipamento = editText_equipamento.text.toString()
                val defeito = editText_defeito.text.toString()

                val imm =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                            InputMethodManager?

                imm?.hideSoftInputFromWindow(
                    requireActivity().currentFocus?.windowToken,
                    0
                )

                if (equipamento.isNotBlank() && defeito.isNotBlank() &&
                    editText_quantidade.text.isNotBlank()) {
                    val quantidade = editText_quantidade.text.toString().toInt()

                    progressBar.visibility = View.VISIBLE

                    val prefs = requireActivity().getSharedPreferences(
                        "UnidadeSaude",
                        0
                    )

                    val unidadeSaude = prefs.getString("Unidade", "")!!
                    val local = prefs.getString("Local", "")!!
                    val name = prefs.getString("Name", "")!!
                    val email = prefs.getString("Email", "")!!

                    GlobalScope.launch(Dispatchers.IO) {
                        transaction {
                            if (!connection.isClosed) {
                                Equipamentos.insertIgnore {
                                    it[Equipamentos.unidade_saude] = unidadeSaude
                                    it[Equipamentos.local] = local
                                    it[Equipamentos.equipamento] = equipamento
                                    it[Equipamentos.defeito] = defeito
                                    it[Equipamentos.quantidade] = quantidade
                                    it[Equipamentos.profissional] = name
                                    it[Equipamentos.email] = email
                                }

                                GlobalScope.launch(Dispatchers.Main) {
                                    progressBar.visibility = View.GONE

                                    Snackbar.make(
                                        main_layout_add, "Dados Inseridos!",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun hasUserInfo(): Boolean {
        val prefs = requireActivity().getSharedPreferences(
            "UnidadeSaude",
            0
        )

        val unidadeSaude = prefs.getString("Unidade", "")!!
        val local = prefs.getString("Local", "")!!
        val name = prefs.getString("Name", "")!!
        val email = prefs.getString("Email", "")!!

        return !(name.isBlank() || email.isBlank() || unidadeSaude.isBlank() || local.isBlank())
    }
}
