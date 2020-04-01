package com.wwmm.sostecsaude.ui.relatar_danos

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
import com.google.android.material.snackbar.Snackbar
import com.wwmm.sostecsaude.R
import com.wwmm.sostecsaude.myServerURL
import kotlinx.android.synthetic.main.fragment_relatar_danos_ver_pedidos.*

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

        val controller = findNavController()

        recyclerview.apply {
            setHasFixedSize(true)

            layoutManager = LinearLayoutManager(requireContext())
        }

        val prefs = requireActivity().getSharedPreferences(
            "UserInfo",
            0
        )

        val token = prefs.getString("Token", "")!!

        val queue = Volley.newRequestQueue(requireContext())

        val request = object : StringRequest(
            Request.Method.POST, "$myServerURL/unidade_saude_pegar_equipamentos",
            Response.Listener { response ->
                val msg = response.toString()

                if (msg == "invalid_token") {
                    controller.navigate(R.id.action_verPedidos_to_fazerLogin)
                } else {
                    val arr = msg.split("<&>")

                    if (arr.isNotEmpty()) {
                        if (isAdded) {
                            recyclerview.apply {
                                adapter = Adapter(arr, progressBar)
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

//        GlobalScope.launch(Dispatchers.IO) {
//            transaction {
//                if (!connection.isClosed) {
//                    val lines = ArrayList<ResultRow>()
//
//                    val query = Equipamentos.select {
//                        Equipamentos.profissional.eq(name) and
//                                Equipamentos.email.eq(email) and
//                                Equipamentos.unidade_saude.eq(unidadeSaude)
//                    }
//
//                    for (line in query) {
//                        lines.add(line)
//                    }
//
//                    GlobalScope.launch(Dispatchers.Main) {
//                        if (isAdded) {
//                            recyclerview.apply {
//                                adapter =
//                                    Adapter(
//                                        lines,
//                                        progressBar
//                                    )
//                            }
//
//                            progressBar.visibility = View.GONE
//                        }
//                    }
//                }
//            }
//        }
    }

    companion object {
        const val LOGTAG = "relatar ver pedidos"
    }
}
