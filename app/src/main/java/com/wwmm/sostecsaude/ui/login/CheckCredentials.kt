package com.wwmm.sostecsaude.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.wwmm.sostecsaude.R
import com.wwmm.sostecsaude.myServerURL

class CheckCredentials : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check_credentials, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controller = findNavController()

        val prefs = requireActivity().getSharedPreferences(
            "UserInfo",
            0
        )

        val queue = Volley.newRequestQueue(requireContext())

        val request = object : StringRequest(
            Request.Method.POST, "$myServerURL/check_credentials",
            Response.Listener { response ->
                val msg = response.toString()

                if (msg == "invalid_token") {
                    controller.navigate(R.id.action_checkCredentials_to_login)
                } else {
                    val arr = msg.split("<&>")

                    if (arr.size == 3) {
                        val token = arr[0]
                        val perfil = arr[1]
                        val email = arr[2]

                        val editor = prefs.edit()

                        editor.putString("Token", token)
                        editor.putString("Perfil", perfil)
                        editor.putString("Email", email)

                        editor.apply()

                        when (perfil) {
                            "unidade_saude" -> {
                                controller.navigate(R.id.action_checkCredentials_to_unidadeSaude)
                            }

                            "unidade_manutencao" -> {
                                controller.navigate(R.id.action_checkCredentials_to_unidadeManutencao)
                            }
                        }
                    }
                }
            },
            Response.ErrorListener {
                Log.d(LOGTAG, "request failed: $it")
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()

                params["token"] = prefs.getString("Token", "nao existe token")!!

                return params
            }
        }

        queue.add(request)
    }

    companion object {
        const val LOGTAG = "check credentials"
    }
}
