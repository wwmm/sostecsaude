package com.wwmm.sostecsaude

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.wwmm.sostecsaude.ui.home.HomeFragment
import com.wwmm.sostecsaude.ui.profissionais.ProfissionaisFragment
import com.wwmm.sostecsaude.ui.relatar_danos.RelatarDanosFragment
import org.jetbrains.exposed.sql.Database

class MainActivity : AppCompatActivity(), HomeFragment.Listener, ProfissionaisFragment.Listener {
    private lateinit var mHomeFragment: HomeFragment
    private lateinit var mProfissionaisFragment: ProfissionaisFragment
    private lateinit var mRelatarDanosFragment: RelatarDanosFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mHomeFragment = HomeFragment()
        mProfissionaisFragment = ProfissionaisFragment()
        mRelatarDanosFragment = RelatarDanosFragment()

        // default fragment
        supportFragmentManager.beginTransaction().replace(R.id.mainFrame, mHomeFragment).commit()

        Database.connect(
            "jdbc:mysql://remotemysql.com/mQe0EBGW7O",
            driver = "com.mysql.jdbc.Driver",
            user = "mQe0EBGW7O",
            password = "azsZegvXg6"
        )
    }

    override fun loadProfissionais() {
        supportFragmentManager.beginTransaction().replace(R.id.mainFrame, mProfissionaisFragment)
            .commit()

        title = getString(R.string.title_profissionais)
    }

    override fun loadEmpresas() {
    }

    override fun loadRelatarDano() {
        supportFragmentManager.beginTransaction().replace(R.id.mainFrame, mRelatarDanosFragment)
            .commit()
    }

    override fun loadContato() {
    }

    override fun onBackPressed() {
        when {
            mHomeFragment.isAdded -> {
                super.onBackPressed()
            }

            mRelatarDanosFragment.isAdded -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, mProfissionaisFragment).commit()

                title = getString(R.string.title_profissionais)
            }

            else -> {
                supportFragmentManager.beginTransaction().replace(R.id.mainFrame, mHomeFragment)
                    .commit()

                title = getString(R.string.app_name)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater

        inflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.upload_db -> {
                true
            }

            R.id.reset_db -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
