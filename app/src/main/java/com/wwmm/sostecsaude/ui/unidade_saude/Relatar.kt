package com.wwmm.sostecsaude.ui.unidade_saude

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.wwmm.sostecsaude.R
import com.wwmm.sostecsaude.myServerURL
import kotlinx.android.synthetic.main.fragment_unidade_saude_relatar.*

class Relatar : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_unidade_saude_relatar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controller = findNavController()

        progressBar.visibility = View.GONE

        button_add.setOnClickListener {
            val nome = editText_nome.text.toString()
            val fabricante = editText_fabricante.text.toString()
            val modelo = editText_modelo.text.toString()
            val numeroSerie = editText_numero_serie.text.toString()
            val defeito = editText_defeito.text.toString()

            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                        InputMethodManager?

            imm?.hideSoftInputFromWindow(
                requireActivity().currentFocus?.windowToken,
                0
            )

            if (nome.isNotBlank() && fabricante.isNotBlank() && modelo.isNotBlank()
                && numeroSerie.isNotBlank() && defeito.isNotBlank() &&
                editText_quantidade.text.isNotBlank()
            ) {
                val quantidade = editText_quantidade.text.toString()

                progressBar.visibility = View.VISIBLE

                val prefs = requireActivity().getSharedPreferences(
                    "UserInfo",
                    0
                )

                val token = prefs.getString("Token", "")!!

                val queue = Volley.newRequestQueue(requireContext())

                val request = object : StringRequest(
                    Method.POST, "$myServerURL/unidade_saude_adicionar_equipamento",
                    Response.Listener { response ->
                        if (isAdded) {
                            val msg = response.toString()

                            if (msg == "invalid_token") {
                                controller.navigate(R.id.action_unidadeManutencao_to_fazerLogin)
                            } else {
                                editText_nome.text.clear()
                                editText_fabricante.text.clear()
                                editText_modelo.text.clear()
                                editText_numero_serie.text.clear()
                                editText_quantidade.text.clear()
                                editText_defeito.text.clear()

                                progressBar.visibility = View.GONE

                                Snackbar.make(
                                    main_layout_relatar, msg,
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    Response.ErrorListener {
                        Log.d(LOGTAG, "failed request: $it")
                    }
                ) {
                    override fun getParams(): MutableMap<String, String> {
                        val parameters = HashMap<String, String>()

                        parameters["token"] = token
                        parameters["nome"] = nome
                        parameters["fabricante"] = fabricante
                        parameters["modelo"] = modelo
                        parameters["numero_serie"] = numeroSerie
                        parameters["quantidade"] = quantidade
                        parameters["defeito"] = defeito

                        return parameters
                    }
                }

                queue.add(request)
            } else {
                Snackbar.make(
                    main_layout_relatar, "Preencha todos os campos!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        const val LOGTAG = "relatar defeito"
    }
}
