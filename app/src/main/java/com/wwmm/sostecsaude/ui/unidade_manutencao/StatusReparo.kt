package com.wwmm.sostecsaude.ui.unidade_manutencao

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

import com.wwmm.sostecsaude.R
import com.wwmm.sostecsaude.connectionErrorMessage
import com.wwmm.sostecsaude.myServerURL
import kotlinx.android.synthetic.main.fragment_unidade_manutencao_status_reparo.*
import org.json.JSONArray
import org.json.JSONObject

class StatusReparo : Fragment(), SearchView.OnQueryTextListener  {
    private lateinit var mController: NavController
    private lateinit var mQueue: RequestQueue
    private val listNomeUnidade = ArrayList<String>()
    private val listEmailCliente = ArrayList<String>()
    private var mAdapterStatusReparo: AdapterStatusReparo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_unidade_manutencao_status_reparo,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mController = findNavController()

        toolbar.inflateMenu(R.menu.menu_toolbar)
        toolbar.menu.findItem(R.id.menu_atualizar_perfil).isVisible = false
        toolbar.menu.findItem(R.id.menu_login).isVisible = false

        val actionView = toolbar.menu.findItem(R.id.menu_search).actionView as
                SearchView

        actionView.setOnQueryTextListener(this)

        spinner_unidade_saude.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                updateRecycler(listEmailCliente[i])
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        recyclerview.apply {
            setHasFixedSize(true)

            layoutManager = LinearLayoutManager(requireContext())
        }

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val token = prefs.getString("Token", "")!!

        val jsonToken = JSONArray()

        jsonToken.put(0, token)

        mQueue = Volley.newRequestQueue(requireContext())

        val request = JsonArrayRequest(
            Request.Method.POST,
            "$myServerURL/lista_clientes_manutencao",
            jsonToken,
            Response.Listener { response ->
                if (isAdded) {
                    if (response.length() > 0) {
                        if (response[0] == "invalid_token") {
                            mController.navigate(R.id.action_global_fazerLogin)
                        } else if (response.length() == 2) {
                            val unidades = response[0] as? JSONArray

                            if (unidades != null) {
                                for(n in 0 until unidades.length()){
                                    val unidade = unidades[n] as JSONObject
                                    val nome = unidade["Nome"] as String
                                    val emailCliente = unidade["Email"] as String

                                    listNomeUnidade.add(nome)
                                    listEmailCliente.add(emailCliente)
                                }

                                val adapter = ArrayAdapter(
                                    requireContext(),
                                    android.R.layout.simple_spinner_dropdown_item, listNomeUnidade
                                )

                                spinner_unidade_saude.adapter = adapter
                            }

                            progressBar.visibility = View.GONE
                        }
                    }
                }
            },
            Response.ErrorListener {
                Log.d(LOGTAG, "failed request: $it")

                if (isAdded) {
                    progressBar.visibility = View.GONE
                }

                connectionErrorMessage(layout_manutencao_status_reparo, it)
            }
        )

        mQueue.add(request)
    }

    private fun updateRecycler(emailCliente: String) {
        progressBar.visibility = View.VISIBLE

        recyclerview.apply {
            adapter = null
        }

        val prefs = getDefaultSharedPreferences(requireContext())

        val token = prefs.getString("Token", "")!!

        val jsonToken = JSONArray()

        jsonToken.put(0, token)
        jsonToken.put(1, emailCliente)

        val queue = Volley.newRequestQueue(requireContext())

        val request = JsonArrayRequest(
            Request.Method.POST,
            "$myServerURL/lista_equipamentos_cliente",
            jsonToken,
            Response.Listener { response ->
                if (isAdded) {
                    println(response.toString())
                    if (response.length() > 0) {
                        if (response[0] == "invalid_token") {
                            mController.navigate(R.id.action_global_fazerLogin)
                        } else {
                            if (response[0] != "empty") {
                                mAdapterStatusReparo = AdapterStatusReparo(this,response)

                                recyclerview.apply {
                                    adapter = mAdapterStatusReparo
                                }
                            }

                            progressBar.visibility = View.GONE
                        }
                    }
                }
            },
            Response.ErrorListener {
                Log.d(LOGTAG, "failed request: $it")

                if (isAdded) {
                    progressBar.visibility = View.GONE
                }

                connectionErrorMessage(layout_manutencao_status_reparo, it)
            }
        )

        queue.add(request)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        mAdapterStatusReparo?.filter?.filter(newText)

        return true
    }

    companion object {
        const val LOGTAG = "StatusReparo"
    }
}
