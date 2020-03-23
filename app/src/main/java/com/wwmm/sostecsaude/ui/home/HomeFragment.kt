package com.wwmm.sostecsaude.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wwmm.sostecsaude.MainActivity
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private var mListener: Listener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mListener = requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_load_profissionais.setOnClickListener {
            mListener?.loadProfissionais()
        }

        button_load_empresas.setOnClickListener {
            mListener?.loadEmpresas()
        }
    }

    interface Listener {
        fun loadProfissionais()
        fun loadEmpresas()
    }
}
