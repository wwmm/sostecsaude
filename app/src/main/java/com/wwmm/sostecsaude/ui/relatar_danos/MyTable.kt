package com.wwmm.sostecsaude.ui.relatar_danos

import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.wwmm.sostecsaude.SQLHelper

class MyTable(private val mCtx: Context, private val mTable: TableLayout) {
    private val mSqldb: SQLHelper = SQLHelper(mCtx)

    fun build(query_str: String) {
        val cursor = mSqldb.dbQuery(query_str)

        mTable.removeAllViews()

        mTable.setBackgroundColor(Color.WHITE)

        buildHeader(cursor)

        val rowLayout = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )

        rowLayout.setMargins(0, 10, 0, 10)

        if (cursor.count != 0 && cursor.moveToFirst()) {
            val nCols = cursor.columnCount

            do {
                val row = TableRow(mCtx)

                row.layoutParams = rowLayout

                row.setBackgroundColor(Color.LTGRAY)

                val color = Color.TRANSPARENT

                for (j in 1 until nCols) {
                    row.addView(
                        buildField(
                            cursor.getString(j), color, 16, Gravity.CENTER,
                            false
                        )
                    )
                }

                mTable.addView(row)
            } while (cursor.moveToNext())
        }
    }

    private fun buildField(
        text: String,
        color: Int,
        size: Int,
        gravity: Int,
        caps: Boolean
    ): TextView {
        val tv = TextView(mCtx)

        val params = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        tv.layoutParams = params

        tv.gravity = gravity

        tv.textSize = size.toFloat()

        tv.setPadding(10, 5, 10, 5)

        tv.setBackgroundColor(color)

        tv.isAllCaps = caps

        tv.text = text

        return tv
    }

    private fun buildHeader(cursor: Cursor) {
        val rowLayout = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )

        rowLayout.setMargins(0, 5, 0, 5)

        var row = TableRow(mCtx)

        //title
        row.layoutParams = rowLayout

        row.id = View.generateViewId()

        row.addView(
            buildField(
                "Equipamentos ou peças com defeito", Color.WHITE, 20,
                Gravity.CENTER, true
            )
        )

        row.gravity = Gravity.CENTER_HORIZONTAL

        mTable.addView(row)

        //Table header
        val nCols = cursor.columnCount

        row = TableRow(mCtx)

        row.layoutParams = rowLayout

        row.id = View.generateViewId()

        val color = Color.CYAN

        for (j in 1 until nCols) {
            row.addView(
                buildField(
                    cursor.getColumnName(j), color, 16, Gravity.CENTER,
                    true
                )
            )
        }

        mTable.addView(row)
    }
}
