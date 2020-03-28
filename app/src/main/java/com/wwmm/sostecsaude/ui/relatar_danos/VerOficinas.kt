package com.wwmm.sostecsaude.ui.relatar_danos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.wwmm.sostecsaude.Empresas
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.fragment_relatar_danos_ver_oficinas.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class VerOficinas : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_relatar_danos_ver_oficinas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerview.apply {
            setHasFixedSize(true)

            layoutManager = LinearLayoutManager(requireContext())
        }

        GlobalScope.launch(Dispatchers.IO) {
            transaction {
                if (!connection.isClosed) {
                    val lines = ArrayList<ResultRow>()

                    for (line in Empresas.selectAll()) {
                        lines.add(line)
                    }

                    GlobalScope.launch(Dispatchers.Main) {
                        if (isAdded) {
                            recyclerview.apply {
                                adapter =
                                    AdapterVerOficinas(lines)
                            }

                            progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }
}
