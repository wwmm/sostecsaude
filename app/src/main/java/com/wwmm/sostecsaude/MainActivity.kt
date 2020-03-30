package com.wwmm.sostecsaude

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.wwmm.sostecsaude.ui.login.Login
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var mLogin: Login? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_nav.visibility = View.GONE

        val controller = Navigation.findNavController(this, R.id.nav_host_main)

        toolbar.setupWithNavController(controller)

        bottom_nav.setupWithNavController(controller)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mLogin?.goBack()

            return true
        }

        return super.onKeyDown(keyCode, event)
    }
}
