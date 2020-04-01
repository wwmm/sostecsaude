package com.wwmm.sostecsaude

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import org.jetbrains.exposed.sql.Table

object Equipamentos : Table("equipamentos") {
    val id = Equipamentos.integer("id").autoIncrement()
    val unidade_saude = Equipamentos.varchar("unidade_saude", 255)
    val local = Equipamentos.varchar("local", 255)
    val profissional = Equipamentos.varchar("profissional", 255)
    val email = Equipamentos.varchar("email", 255)
    val equipamento = Equipamentos.varchar("equipamento", 255)
    val fabricante = Equipamentos.varchar("fabricante", 255)
    val modelo = Equipamentos.varchar("modelo", 255)
    val numero_serie = Equipamentos.varchar("numero_serie", 255)
    val defeito = Equipamentos.varchar("defeito", 255)
    val quantidade = Equipamentos.integer("quantidade")

    override val primaryKey = PrimaryKey(id)
}

object Empresas : Table("empresas") {
    val id = Empresas.integer("id").autoIncrement()
    val nome = Empresas.varchar("nome", 255)
    val setor = Empresas.varchar("setor", 255)
    val local = Empresas.varchar("local", 255)
    val contato = Empresas.varchar("contato", 255)

    override val primaryKey = PrimaryKey(id)
}

const val myServerURL = "http://albali.eic.cefet-rj.br:8082"
