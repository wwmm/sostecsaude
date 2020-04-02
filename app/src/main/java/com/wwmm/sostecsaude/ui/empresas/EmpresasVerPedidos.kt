package com.wwmm.sostecsaude.ui.empresas

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.wwmm.sostecsaude.Equipamentos
import com.wwmm.sostecsaude.R
import com.wwmm.sostecsaude.myServerURL
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
        return inflater.inflate(R.layout.fragment_empresas_ver_pedidos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerview.apply {
            setHasFixedSize(true)

            layoutManager = LinearLayoutManager(requireContext())
        }

        val controller = findNavController()

        val prefs = requireActivity().getSharedPreferences(
            "UserInfo",
            0
        )

        val token = prefs.getString("Token", "")!!

        val queue = Volley.newRequestQueue(requireContext())

        val request = object : StringRequest(
            Request.Method.POST, "$myServerURL/pegar_todos_equipamentos",
            Response.Listener { response ->
                val msg = response.toString()

                if (msg == "invalid_token") {
                    controller.navigate(R.id.action_empresasVerPedidos_to_fazerLogin)
                } else {
                    val arr = msg.split("<&>")

                    if (arr.isNotEmpty()) {
                        if (isAdded) {
                            recyclerview.apply {
                                adapter = Adapter(this@EmpresasVerPedidos, arr)
                            }
                        }
                    }

                    progressBar.visibility = View.GONE
                }
            },
            Response.ErrorListener {
                Log.d(LOGTAG, "failed request: relatar defeito")
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val parameters = HashMap<String, String>()

                parameters["token"] = token

                return parameters
            }
        }

        queue.add(request)
    }

    companion object {
        const val LOGTAG = "manutencao ver pedidos"
    }
}
