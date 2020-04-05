package com.wwmm.sostecsaude.ui.unidade_manutencao

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.wwmm.sostecsaude.R
import com.wwmm.sostecsaude.myServerURL
import kotlinx.android.synthetic.main.fragment_unidade_manutencao.*
import org.json.JSONArray

class UnidadeManutencao : Fragment(), Toolbar.OnMenuItemClickListener, SearchView.OnQueryTextListener {
    private lateinit var mActivityController: NavController
    private var mAdapter: Adapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_unidade_manutencao, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mActivityController = Navigation.findNavController(requireActivity(), R.id.nav_host_main)

        val controller = findNavController()

        toolbar.setupWithNavController(findNavController())
        toolbar.inflateMenu(R.menu.menu_toolbar)
        toolbar.setOnMenuItemClickListener(this)

        val actionView = toolbar.menu.findItem(R.id.menu_search).actionView as
                SearchView

        actionView.setOnQueryTextListener(this)

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
            "$myServerURL/lista_todos_equipamentos",
            jsonToken,
            Response.Listener { response ->
                if (isAdded) {
                    if (response.length() > 0) {
                        if (response[0] == "invalid_token") {
                            controller.navigate(R.id.action_unidadeManutencao_to_fazerLogin)
                        } else {
                            if (response.length() == 2) {
                                val equipamentos = response[0] as JSONArray
                                val idNumbers = response[1] as JSONArray

                                mAdapter = Adapter(this@UnidadeManutencao, equipamentos,
                                    idNumbers)

                                recyclerview.apply {
                                    adapter = mAdapter
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

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fazerLogin -> {
                val prefs = requireContext().getSharedPreferences(
                    "UserInfo",
                    0
                )

                val editor = prefs.edit()

                editor.putString("Token", "")
                editor.putString("Perfil", "")
                editor.putString("Email", "")

                editor.apply()

                return item.onNavDestinationSelected(mActivityController)
            }

            R.id.menu_atualizar_perfil -> {
                mActivityController.navigate(R.id.action_global_unidadeManutencao)

                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        mAdapter?.filter?.filter(newText)

        return true
    }

    companion object {
        const val LOGTAG = "manutencao ver pedidos"
    }
}
