package com.wwmm.sostecsaude.ui.profissionais

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.wwmm.sostecsaude.MainActivity
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.fragment_contato.*


class ContatoFragment : Fragment() {
    private var mListener: Listener? = null
    var mLoadHome = false

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mListener = requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contato, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity().getSharedPreferences(
            "UserInfo",
            0
        )

        val name = prefs.getString("Name", "")
        val email = prefs.getString("Email", "")

        editText_nome.setText(name)
        editText_email.setText(email)

        button_save.setOnClickListener {
            val editor = prefs.edit()

            editor.putString("Name", editText_nome.text.toString())
            editor.putString("Email", editText_email.text.toString())

            editor.apply()

            val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                    InputMethodManager?

            imm?.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)

            Snackbar.make(
                main_layout_contato, "Dados Salvos!",
                Snackbar.LENGTH_SHORT
            ).show()

            if (mLoadHome) {
                mListener?.loadHome()
            }
        }
    }

    interface Listener {
        fun loadHome()
    }
}
