package com.wwmm.sostecsaude.ui.relatar_danos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
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

        val navController = Navigation.findNavController(
            requireActivity(),
            R.id.nav_host_fragment
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_add -> {
                    requireActivity().title = getString(R.string.title_relatar_danos)
                }

                R.id.navigation_list -> {
                    requireActivity().title = getString(R.string.title_ver_danos)
                }

                else -> {
                }
            }
        }

        nav_view.setupWithNavController(navController)
    }
}
