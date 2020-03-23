package com.wwmm.sostecsaude

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        var str = "create table $DB_TABLE_NAME "

        str += "($DB_ID integer primary key, "
        str += "$DB_LOCAL text, "
        str += "$DB_EQUIPAMENTO text, "
        str += "$DB_DEFEITO text, "
        str += "$DB_QUANTIDADE integer)"

        db.execSQL(str)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //        db.execSQL("DROP TABLE IF EXISTS diario");
        //        onCreate(db);
    }

    fun insert(local: String, equipamento: String, defeito: String, quantidade: Int): Boolean {
        val db = this.writableDatabase

        val contentValues = ContentValues()

        contentValues.put(DB_LOCAL, local)
        contentValues.put(DB_EQUIPAMENTO, equipamento)
        contentValues.put(DB_DEFEITO, defeito)
        contentValues.put(DB_QUANTIDADE, quantidade)

        db.insert(DB_TABLE_NAME, null, contentValues)

        return true
    }

    fun dbQuery(query_str: String): Cursor {
        val db = this.readableDatabase

        return db.rawQuery(query_str, null)
    }

    fun rawQuery(query: String, args: Array<String>): Cursor {
        val db = this.readableDatabase

        return db.rawQuery(query, args)
    }

    fun update(id: Int, local: String, equipamento: String, defeito: String, quantidade: Int) {
        val db = this.writableDatabase

        val newValues = ContentValues()

        newValues.put(DB_LOCAL, local)
        newValues.put(DB_EQUIPAMENTO, equipamento)
        newValues.put(DB_DEFEITO, defeito)
        newValues.put(DB_QUANTIDADE, quantidade)

        db.update(DB_TABLE_NAME, newValues, "$DB_ID=?", arrayOf(id.toString()))
    }

    fun deleteSelection(id: Int?): Int? {
        val db = this.writableDatabase

        return db.delete(DB_TABLE_NAME, "_id = ? ", arrayOf((id!!).toString()))
    }

    fun deleteAll(): Int? {
        val db = this.writableDatabase

        return db.delete(DB_TABLE_NAME, null, null)
    }

    fun getCount(): Long {
        val db = this.readableDatabase

        val count = DatabaseUtils.queryNumEntries(db, DB_TABLE_NAME)

        db.close()

        return count
    }

    companion object {
        const val DB_NAME = "comdefeito.db"
        private const val DB_VERSION = 1
        const val DB_TABLE_NAME = "equipamentos"
        const val DB_ID = "_id"
        const val DB_LOCAL = "local"
        const val DB_EQUIPAMENTO = "equipamento"
        const val DB_DEFEITO = "defeito"
        const val DB_QUANTIDADE = "quantidade"
    }
}
