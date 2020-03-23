package com.wwmm.sostecsaude.ui.relatar_danos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            val local = editText_local.text.toString()
            val equipamento = editText_equipamento.text.toString()
            val defeito = editText_defeito.text.toString()

            if (local.isNotEmpty() && equipamento.isNotEmpty() && defeito.isNotEmpty() &&
                editText_quantidade.text.isNotEmpty()
            ) {
                val quantidade = editText_quantidade.text.toString().toInt()

                progressBar.visibility = View.VISIBLE

                GlobalScope.launch(Dispatchers.IO) {
                    transaction {
                        if (!connection.isClosed) {
                            Equipamentos.insertIgnore {
                                it[Equipamentos.local] = local
                                it[Equipamentos.equipamento] = equipamento
                                it[Equipamentos.defeito] = defeito
                                it[Equipamentos.quantidade] = quantidade
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
