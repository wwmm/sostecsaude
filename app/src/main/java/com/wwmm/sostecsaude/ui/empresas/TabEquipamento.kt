package com.wwmm.sostecsaude.ui.empresas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.fragment_tab_equipamento.*

class TabEquipamento : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab_equipamento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments.apply {
            textView_equipamento.text = this!!.getString("Equipamento").toString()
            textView_fabricante.text = this.getString("Fabricante").toString()
            textView_modelo.text = this.getString("Modelo").toString()
            textView_numero_serie.text = this.getString("NumeroSerie").toString()
        }
    }
}
