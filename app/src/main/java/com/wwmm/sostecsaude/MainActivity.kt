package com.wwmm.sostecsaude

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.exposed.sql.Database

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_nav.visibility = View.GONE

        val controller = Navigation.findNavController(this, R.id.nav_host_main)

        toolbar.setupWithNavController(controller)

        bottom_nav.setupWithNavController(controller)
    }
}
