package com.wwmm.sostecsaude.ui.relatar_danos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.wwmm.sostecsaude.Equipamentos
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerview.apply {
            setHasFixedSize(true)

            layoutManager = LinearLayoutManager(requireContext())
        }

        GlobalScope.launch(Dispatchers.IO) {
            transaction {
                addLogger(StdOutSqlLogger)

                if (!connection.isClosed) {
                    val lines = ArrayList<ResultRow>()

                    for (line in Equipamentos.selectAll()) {
                        lines.add(line)
                    }

                    GlobalScope.launch(Dispatchers.Main) {
                        if(isAdded) {
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
