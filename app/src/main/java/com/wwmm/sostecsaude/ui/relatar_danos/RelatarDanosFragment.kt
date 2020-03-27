package com.wwmm.sostecsaude.ui.relatar_danos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.fragment_relatar_danos.*

class RelatarDanosFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_relatar_danos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val fragmentContainer = view.findViewById<View>(R.id.nav_host_relatar)

//        val navController = Navigation.findNavController(fragmentContainer)

//        val navController = childFragmentManager.findFragmentById(R.id.nav_host_relatar) as NavHostFragment

//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.navigation_add -> {
//                    requireActivity().title = getString(R.string.title_relatar_danos)
//                }
//
//                R.id.navigation_list -> {
//                    requireActivity().title = getString(R.string.title_ver_danos)
//                }
//
//                R.id.navigation_contato -> {
//                    requireActivity().title = getString(R.string.title_contato)
//                }
//
//                else -> {
//                }
//            }
//        }

//        nav_view.setupWithNavController(navController.navController)

//        nav_view.setOnNavigationItemSelectedListener {
//            when(it.itemId){
//                R.id.navigation_add -> {
//                    requireActivity().title = getString(R.string.title_relatar_danos)
//                }
//
//                R.id.navigation_list -> {
//                    requireActivity().title = getString(R.string.title_ver_danos)
//                }
//
//                R.id.navigation_contato -> {
//                    requireActivity().title = getString(R.string.title_contato)
//                }
//            }
//
//            true
//        }
    }
}
