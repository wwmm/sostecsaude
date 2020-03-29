package com.wwmm.sostecsaude.ui.contatos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.wwmm.sostecsaude.Empresas
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.fragment_contatos_unidade_manutencao.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.transactions.transaction

class UnidadeManutencao : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contatos_unidade_manutencao, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar.visibility = View.GONE

        val prefs = requireActivity().getSharedPreferences(
            "UnidadeManutencao",
            0
        )

        var nome = prefs.getString("Nome", "")!!
        var setor = prefs.getString("Setor", "")!!
        var local = prefs.getString("Local", "")!!
        val contato = prefs.getString("Email", "")!!

        editText_nome.setText(nome)
        editText_setor.setText(setor)
        editText_local.setText(local)

        button_empresa_contato.setOnClickListener {
            nome = editText_nome.text.toString()
            setor = editText_setor.text.toString()
            local = editText_local.text.toString()

            if (nome.isBlank() || setor.isBlank() || local.isBlank()) {
                Snackbar.make(
                    main_layout_empresa_contato, "Preencha todos os campos!",
                    Snackbar.LENGTH_SHORT
                ).show()

            } else {
                progressBar.visibility = View.VISIBLE

                val editor = prefs.edit()

                editor.putString("Nome", nome)
                editor.putString("Setor", setor)
                editor.putString("Local", local)

                editor.apply()

                GlobalScope.launch(Dispatchers.IO) {
                    transaction {
                        if (!connection.isClosed) {
                            Empresas.insertIgnore {
                                it[Empresas.nome] = nome
                                it[Empresas.setor] = setor
                                it[Empresas.local] = local
                                it[Empresas.contato] = contato
                            }

                            GlobalScope.launch(Dispatchers.Main) {
                                progressBar.visibility = View.GONE

                                Snackbar.make(
                                    main_layout_empresa_contato, "Dados Inseridos!",
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
