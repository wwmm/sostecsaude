package com.wwmm.sostecsaude

import org.jetbrains.exposed.sql.Table

object Equipamentos : Table("equipamentos") {
    val id = Equipamentos.integer("id").autoIncrement()
    val local = Equipamentos.varchar("local", 255)
    val equipamento = Equipamentos.varchar("equipamento", 255)
    val defeito = Equipamentos.varchar("defeito", 255)
    val quantidade = Equipamentos.integer("quantidade")

    override val primaryKey = PrimaryKey(id)
}
