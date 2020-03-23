package com.wwmm.sostecsaude

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class MainActivity : AppCompatActivity() {
    private lateinit var mSqldb: SQLHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_add, R.id.navigation_list, R.id.navigation_export
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        mSqldb = SQLHelper(this)

        Database.connect(
            "jdbc:mysql://remotemysql.com/mQe0EBGW7O",
            driver = "com.mysql.jdbc.Driver",
            user = "mQe0EBGW7O",
            password = "azsZegvXg6"
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater

        inflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.upload_db -> {
                GlobalScope.launch(Dispatchers.IO) {
                    val cursor = mSqldb.dbQuery(
                        "select * from ${SQLHelper.DB_TABLE_NAME}")

                    if (cursor.count != 0) {
                        cursor.moveToFirst()

                        do {
                            val local = cursor.getString(cursor.getColumnIndex(
                                SQLHelper.DB_LOCAL))

                            val equipamento = cursor.getString(cursor.getColumnIndex(
                                SQLHelper.DB_EQUIPAMENTO))

                            val defeito = cursor.getString(cursor.getColumnIndex(
                                SQLHelper.DB_DEFEITO))

                            val quantidade = cursor.getString(cursor.getColumnIndex(
                                SQLHelper.DB_QUANTIDADE)).toInt()

                            transaction {
                                addLogger(StdOutSqlLogger)

                                if (!connection.isClosed) {
                                    RemoteServer.Equipamentos.insertIgnore {
                                        it[Equipamentos.local] = local
                                        it[Equipamentos.equipamento] = equipamento
                                        it[Equipamentos.defeito] = defeito
                                        it[Equipamentos.quantidade] = quantidade
                                    }
                                }
                            }
                        } while (cursor.moveToNext())
                    }
                }

                true
            }

            R.id.reset_db -> {
                deleteDatabase(SQLHelper.DB_NAME)

                mSqldb = SQLHelper(this)

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
