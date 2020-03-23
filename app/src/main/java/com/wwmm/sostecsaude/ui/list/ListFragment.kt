package com.wwmm.sostecsaude.ui.list

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.wwmm.sostecsaude.Equipamentos
import com.wwmm.sostecsaude.R
import com.wwmm.sostecsaude.SQLHelper
import com.wwmm.sostecsaude.RemoteServer
import com.wwmm.sostecsaude.RemoteServer.Equipamentos.autoIncrement
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class ListFragment : Fragment() {
    private lateinit var mSqldb: SQLHelper
    private lateinit var mRemoteServer: RemoteServer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSqldb = SQLHelper(requireContext())

        mRemoteServer = RemoteServer()
    }

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

                val lines = ArrayList<ResultRow>()

                for(line in Equipamentos.selectAll()){
                    lines.add(line)
                }

                GlobalScope.launch(Dispatchers.Main) {
                    recyclerview.apply {
                        adapter = Adapter(lines)
                    }

                    progressBar.visibility = View.GONE
                }
            }
        }
    }
}
