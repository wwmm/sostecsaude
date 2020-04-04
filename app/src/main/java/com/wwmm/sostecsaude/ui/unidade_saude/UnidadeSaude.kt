package com.wwmm.sostecsaude.ui.unidade_saude

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.fragment_unidade_saude.*

class UnidadeSaude : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_unidade_saude, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controller = Navigation.findNavController(
            requireActivity(),
            R.id.nav_host_unidade_saude
        )

        bottom_nav.setupWithNavController(controller)

//        val actionView = toolbar_top.menu.findItem(R.id.menu_search).actionView as
//                SearchView
//
//        actionView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(p0: String?): Boolean {
//                println("search submit")
//
//                return true
//            }
//
//            override fun onQueryTextChange(p0: String?): Boolean {
//                println("search change")
//
//                return true
//            }
//        })
    }
}
