package com.wwmm.sostecsaude.ui.empresas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wwmm.sostecsaude.Equipamentos
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.fragment_empresas_ver_pedidos.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class EmpresasVerPedidos : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_empresas_ver_pedidos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerview.apply {
            setHasFixedSize(true)

            layoutManager = LinearLayoutManager(requireContext())
        }

        val controller = findNavController()

        if (!hasUserInfo()) {
            controller.navigate(R.id.action_global_unidadeManutencao)
        } else {
            GlobalScope.launch(Dispatchers.IO) {
                transaction {
                    if (!connection.isClosed) {
                        val lines = ArrayList<ResultRow>()

                        for (line in Equipamentos.selectAll()) {
                            lines.add(line)
                        }

                        GlobalScope.launch(Dispatchers.Main) {
                            if (isAdded) {
                                recyclerview.apply {
                                    adapter =
                                        Adapter(requireParentFragment(), lines)
                                }

                                progressBar.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }
    }

    private fun hasUserInfo(): Boolean {
        val prefs = requireActivity().getSharedPreferences(
            "UnidadeManutencao",
            0
        )

        val nome = prefs.getString("Nome", "")!!
        val setor = prefs.getString("Setor", "")!!
        val local = prefs.getString("Local", "")!!
        val contato = prefs.getString("Email", "")!!

        return !(nome.isBlank() || setor.isBlank() || local.isBlank() || contato.isBlank())
    }
}
