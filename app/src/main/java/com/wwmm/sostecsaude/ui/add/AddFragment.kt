package com.wwmm.sostecsaude.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wwmm.sostecsaude.R
import com.wwmm.sostecsaude.SQLHelper
import kotlinx.android.synthetic.main.fragment_add.*

class AddFragment : Fragment() {
    private lateinit var mSqldb: SQLHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSqldb = SQLHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_add.setOnClickListener {
            val local = editText_local.text.toString()
            val equipamento = editText_equipamento.text.toString()
            val defeito = editText_defeito.text.toString()

            if (local.isNotEmpty() && equipamento.isNotEmpty() && defeito.isNotEmpty() &&
                editText_quantidade.text.isNotEmpty()
            ) {
                val quantidade = editText_quantidade.text.toString().toInt()

                mSqldb.insert(local, equipamento, defeito, quantidade)
            }
        }
    }
}
