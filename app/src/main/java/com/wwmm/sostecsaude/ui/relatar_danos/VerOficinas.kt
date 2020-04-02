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
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.wwmm.sostecsaude.R
import com.wwmm.sostecsaude.myServerURL
import kotlinx.android.synthetic.main.fragment_relatar_danos_ver_oficinas.*
import org.json.JSONArray

class VerOficinas : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_relatar_danos_ver_oficinas, container, false)
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

        val jsonToken = JSONArray()

        jsonToken.put(0, token)

        val queue = Volley.newRequestQueue(requireContext())

        val request = JsonArrayRequest(
            Request.Method.POST,
            "$myServerURL/unidade_saude_pegar_oficinas_interessadas",
            jsonToken,
            Response.Listener { response ->
                if (response.length() > 0) {
                    if (response[0] == "invalid_token") {
                        controller.navigate(R.id.action_verPedidos_to_fazerLogin)
                    } else {
                        if (isAdded) {
                            recyclerview.apply {
//                                adapter = AdapterVerOficinas(this@VerOficinas, response)
                            }
                        }

                        progressBar.visibility = View.GONE
                    }
                }
            },
            Response.ErrorListener {
                Log.d(LOGTAG, "failed request: $it")
            }
        )

        queue.add(request)

//        GlobalScope.launch(Dispatchers.IO) {
//            transaction {
//                if (!connection.isClosed) {
//                    val lines = ArrayList<ResultRow>()
//
//                    for (line in Empresas.selectAll()) {
//                        lines.add(line)
//                    }
//
//                    GlobalScope.launch(Dispatchers.Main) {
//                        if (isAdded) {
//                            recyclerview.apply {
//                                adapter =
//                                    AdapterVerOficinas(lines)
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
        const val LOGTAG = "relatar ver oficinas"
    }
}
