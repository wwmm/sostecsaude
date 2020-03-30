package com.wwmm.sostecsaude

import android.os.Bundle
import android.view.*
import android.webkit.CookieManager
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

        setSupportActionBar(toolbar)

        bottom_nav.setupWithNavController(controller)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mLogin != null) {
                if (mLogin?.isAdded!!) {
                    mLogin?.goBack()

                    return true
                }
            }
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater

        inflater.inflate(R.menu.menu_main_activity, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_activity_sair -> {
                if (mLogin != null) {
                    if (mLogin?.isAdded!!) {
                        return true
                    }
                }

                val manager= CookieManager.getInstance()

                manager.removeAllCookies(null)
                manager.removeSessionCookies(null)

                val intent = intent
                finish()
                startActivity(intent)

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
