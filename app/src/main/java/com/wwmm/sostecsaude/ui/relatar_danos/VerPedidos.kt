package com.wwmm.sostecsaude.ui.relatar_danos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.wwmm.sostecsaude.Equipamentos
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.fragment_relatar_danos_ver_pedidos.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class VerPedidos : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_relatar_danos_ver_pedidos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerview.apply {
            setHasFixedSize(true)

            layoutManager = LinearLayoutManager(requireContext())
        }

        val prefs = requireActivity().getSharedPreferences(
            "UnidadeSaude",
            0
        )

        val unidadeSaude = prefs.getString("Unidade", "")!!
        val name = prefs.getString("Name", "")!!
        val email = prefs.getString("Email", "")!!

        GlobalScope.launch(Dispatchers.IO) {
            transaction {
//                addLogger(StdOutSqlLogger)

                if (!connection.isClosed) {
                    val lines = ArrayList<ResultRow>()

                    val query = Equipamentos.select {
                        Equipamentos.profissional.eq(name) and
                                Equipamentos.email.eq(email) and
                                Equipamentos.unidade_saude.eq(unidadeSaude)
                    }

                    for (line in query) {
                        lines.add(line)
                    }

                    GlobalScope.launch(Dispatchers.Main) {
                        if (isAdded) {
                            recyclerview.apply {
                                adapter =
                                    Adapter(
                                        lines,
                                        progressBar
                                    )
                            }

                            progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }
}
