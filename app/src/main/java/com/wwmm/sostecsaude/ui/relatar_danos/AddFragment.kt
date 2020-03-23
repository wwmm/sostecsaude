package com.wwmm.sostecsaude.ui.relatar_danos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.wwmm.sostecsaude.Equipamentos
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.transactions.transaction

class AddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar.visibility = View.GONE

        button_add.setOnClickListener {
            val unidadeSaude = editText_unidade_saude.text.toString()
            val local = editText_local.text.toString()
            val equipamento = editText_equipamento.text.toString()
            val defeito = editText_defeito.text.toString()

            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                        InputMethodManager?

            imm?.hideSoftInputFromWindow(
                requireActivity().currentFocus?.windowToken,
                0
            )

            if (local.isNotEmpty() && equipamento.isNotEmpty() && defeito.isNotEmpty() &&
                editText_quantidade.text.isNotEmpty()
            ) {
                val quantidade = editText_quantidade.text.toString().toInt()

                progressBar.visibility = View.VISIBLE

                val prefs = requireActivity().getSharedPreferences(
                    "UserInfo",
                    0
                )

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
