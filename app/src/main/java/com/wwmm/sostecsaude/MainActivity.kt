package com.wwmm.sostecsaude

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var mController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_nav.visibility = View.GONE

        mController = Navigation.findNavController(this, R.id.nav_host_main)

        setSupportActionBar(toolbar)

        toolbar.setupWithNavController(mController)

        bottom_nav.setupWithNavController(mController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater

        inflater.inflate(R.menu.menu_main_activity, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fazerLogin -> {
                val prefs = getSharedPreferences(
                    "UserInfo",
                    0
                )

                val editor = prefs.edit()

                editor.putString("Token", "")

                editor.apply()

                item.onNavDestinationSelected(mController)
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}
