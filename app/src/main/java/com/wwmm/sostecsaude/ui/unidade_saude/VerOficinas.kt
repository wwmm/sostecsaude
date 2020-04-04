package com.wwmm.sostecsaude.ui.unidade_saude

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.wwmm.sostecsaude.R
import com.wwmm.sostecsaude.myServerURL
import kotlinx.android.synthetic.main.fragment_unidade_saude_ver_ofertas.*
import org.json.JSONArray
import org.json.JSONObject

class VerOficinas : Fragment() {
    private lateinit var mController: NavController
    private var mIdList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_unidade_saude_ver_ofertas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mController = findNavController()

        spinner_equipamento.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                updateRecycler(mIdList[i])
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

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
            "$myServerURL/unidade_saude_pegar_equipamentos",
            jsonToken,
            Response.Listener { response ->
                if (response.length() > 0) {
                    if (response[0] == "invalid_token") {
                        mController.navigate(R.id.action_verPedidos_to_fazerLogin)
                    } else {
                        if (isAdded) {
                            val list = ArrayList<String>()
                            mIdList.clear()

                            for(n in 0 until response.length()){
                                val obj = response[n] as JSONObject

                                list.add(obj.getString("Nome"))
                                mIdList.add(obj.getString("ID"))
                            }

                            val adapter = ArrayAdapter(requireContext(),
                                android.R.layout.simple_spinner_dropdown_item, list)

                            spinner_equipamento.adapter = adapter

                            progressBar.visibility = View.GONE
                        }
                    }
                }
            },
            Response.ErrorListener {
                Log.d(LOGTAG, "failed request: $it")
            }
        )

        queue.add(request)
    }

    private fun updateRecycler(idEquipamento: String){
        progressBar.visibility = View.VISIBLE

        recyclerview.apply {
            adapter = null
        }

        val prefs = requireActivity().getSharedPreferences(
            "UserInfo",
            0
        )

        val token = prefs.getString("Token", "")!!

        val jsonToken = JSONArray()

        jsonToken.put(0, token)
        jsonToken.put(1, idEquipamento)

        val queue = Volley.newRequestQueue(requireContext())

        val request = JsonArrayRequest(
            Request.Method.POST,
            "$myServerURL/lista_interessados_manutencao",
            jsonToken,
            Response.Listener { response ->
                if (isAdded) {
                    if (response.length() > 0) {
                        if (response[0] == "invalid_token") {
                            mController.navigate(R.id.action_verPedidos_to_fazerLogin)
                        } else{
                            if(response[0] != "empty"){
                                recyclerview.apply {
                                    adapter = AdapterVerOficinas(response)
                                }
                            }

                            progressBar.visibility = View.GONE
                        }
                    }
                }
            },
            Response.ErrorListener {
                Log.d(LOGTAG, "failed request: $it")
            }
        )

        queue.add(request)
    }

    companion object {
        const val LOGTAG = "relatar ver oficinas"
    }
}
