package com.wwmm.sostecsaude

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wwmm.sostecsaude.ui.empresas.EmpresasFragment
import com.wwmm.sostecsaude.ui.home.HomeFragment
import com.wwmm.sostecsaude.ui.profissionais.ContatoFragment
import com.wwmm.sostecsaude.ui.profissionais.ProfissionaisFragment
import com.wwmm.sostecsaude.ui.relatar_danos.RelatarDanosFragment
import org.jetbrains.exposed.sql.Database

class MainActivity : AppCompatActivity(), HomeFragment.Listener, ProfissionaisFragment.Listener,
    ContatoFragment.Listener {
    private lateinit var mHomeFragment: HomeFragment
    private lateinit var mProfissionaisFragment: ProfissionaisFragment
    private lateinit var mRelatarDanosFragment: RelatarDanosFragment
    private lateinit var mContatoFragment: ContatoFragment
    private lateinit var mEmpresasFragment: EmpresasFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mHomeFragment = HomeFragment()
        mProfissionaisFragment = ProfissionaisFragment()
        mRelatarDanosFragment = RelatarDanosFragment()
        mContatoFragment = ContatoFragment()
        mEmpresasFragment = EmpresasFragment()

        loadHome()

        Database.connect(
            "jdbc:mysql://remotemysql.com/mQe0EBGW7O",
            driver = "com.mysql.jdbc.Driver",
            user = "mQe0EBGW7O",
            password = "azsZegvXg6"
        )
    }

    override fun loadProfissionais() {
        val prefs = getSharedPreferences(
            "UserInfo",
            0
        )

        val name = prefs.getString("Name", "")
        val email = prefs.getString("Email", "")

        title = if (name.isNullOrBlank() || email.isNullOrBlank()) {
            mContatoFragment.mLoadHome = true

            supportFragmentManager.beginTransaction().replace(R.id.mainFrame, mContatoFragment)
                .commit()

            getString(R.string.title_profissional_contato)
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainFrame, mProfissionaisFragment)
                .commit()

            getString(R.string.title_profissionais)
        }
    }

    override fun loadEmpresas() {
        supportFragmentManager.beginTransaction().replace(R.id.mainFrame, mEmpresasFragment)
            .commit()

        title = getString(R.string.title_empresas)
    }

    override fun loadRelatarDano() {
        supportFragmentManager.beginTransaction().replace(R.id.mainFrame, mRelatarDanosFragment)
            .commit()

        title = getString(R.string.title_relatar_danos)
    }

    override fun loadContato() {
        supportFragmentManager.beginTransaction().replace(R.id.mainFrame, mContatoFragment)
            .commit()

        title = getString(R.string.title_profissional_contato)
    }

    override fun loadHome() {
        supportFragmentManager.beginTransaction().replace(R.id.mainFrame, mHomeFragment)
            .commit()

        title = getString(R.string.app_name)
    }

    override fun onBackPressed() {
        when {
            mHomeFragment.isAdded -> {
                super.onBackPressed()
            }

            mRelatarDanosFragment.isAdded || mContatoFragment.isAdded -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, mProfissionaisFragment).commit()

                title = getString(R.string.title_profissionais)
            }

            else -> {
                loadHome()
            }
        }
    }
}
