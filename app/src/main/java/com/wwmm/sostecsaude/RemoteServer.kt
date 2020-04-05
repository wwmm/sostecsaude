package com.wwmm.sostecsaude

import android.view.View
import com.android.volley.NetworkError
import com.android.volley.VolleyError
import com.google.android.material.snackbar.Snackbar

const val myServerURL = "http://albali.eic.cefet-rj.br:8081"

fun connectionErrorMessage(view: View, volleyError: VolleyError) {
    if (volleyError is NetworkError) {
        Snackbar.make(
            view,
            "Sem conexão com o servidor!",
            Snackbar.LENGTH_LONG
        ).show()
    }
}
