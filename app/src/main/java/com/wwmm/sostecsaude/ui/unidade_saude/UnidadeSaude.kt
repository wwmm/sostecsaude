package com.wwmm.sostecsaude.ui.unidade_saude

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.wwmm.sostecsaude.R
import com.wwmm.sostecsaude.myServerURL
import kotlinx.android.synthetic.main.fragment_contatos_unidade_saude.*


class UnidadeSaude : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contatos_unidade_saude, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controller = findNavController()

        val prefs = requireActivity().getSharedPreferences(
            "UserInfo",
            0
        )

        val token = prefs.getString("Token", "")!!

        val queue = Volley.newRequestQueue(requireContext())

        val requestGet = object : StringRequest(
            Request.Method.POST, "$myServerURL/get_unidade",
            Response.Listener { response ->
                val msg = response.toString()

                if (msg == "invalid_token") {
                    controller.navigate(R.id.action_unidadeSaude_to_fazerLogin)
                } else {
                    val arr = msg.split("<&>")

                    if (arr.size == 2) {
                        val nome = arr[0]
                        val local = arr[1]

                        editText_unidade_saude.setText(nome)
                        editText_local.setText(local)
                    }
                }
            },
            Response.ErrorListener {
                Log.d(LOGTAG, "\"failed request: pegar contato de unidade de saúde\"")
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val parameters = HashMap<String, String>()

                parameters["token"] = token

                return parameters
            }
        }

        queue.add(requestGet)

        button_save.setOnClickListener {
            val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                    InputMethodManager?

            imm?.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)

            if (editText_local.text.isNotBlank() && editText_unidade_saude.text.isNotBlank()) {
                val r = object : StringRequest(
                    Method.POST, "$myServerURL/update_unidade",
                    Response.Listener { response ->
                        if (isAdded) {
                            val msg = response.toString()

                            if (msg == "invalid_token") {
                                controller.navigate(R.id.action_unidadeSaude_to_fazerLogin)
                            } else {
                                Snackbar.make(
                                    main_layout_contato, msg,
                                    Snackbar.LENGTH_SHORT
                                ).show()

                                controller.navigate(R.id.action_unidadeSaude_to_carregarPerfil)
                            }
                        }
                    },
                    Response.ErrorListener {
                        Log.d(LOGTAG, "failed request: atualizar contato de unidade de saúde")
                    }) {
                    override fun getParams(): MutableMap<String, String> {
                        val parameters = HashMap<String, String>()

                        parameters["token"] = token
                        parameters["nome"] = editText_unidade_saude.text.toString()
                        parameters["local"] = editText_local.text.toString()

                        return parameters
                    }
                }

                queue.add(r)
            } else {
                Snackbar.make(
                    main_layout_contato, "Preencha todos os campos!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        const val LOGTAG = "contato unidade saude"
    }
}
