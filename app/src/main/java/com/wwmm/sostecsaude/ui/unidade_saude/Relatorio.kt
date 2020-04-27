package com.wwmm.sostecsaude.ui.unidade_saude

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar

import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.fragment_unidade_saude_relatorio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.OutputStream

class Relatorio : Fragment() {
    private lateinit var mController: NavController
    private lateinit var mQueue: RequestQueue
    private lateinit var mPrefs: SharedPreferences
    private var mProcessing = false
    private var mHavePermissions = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions(permissions, REQUEST_PERMISSION_CODE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_unidade_saude_relatorio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mController = findNavController()

        mPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        mQueue = Volley.newRequestQueue(requireContext())

        button_salvar_relatorio.setOnClickListener {
            if (mHavePermissions) {
                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)

                    type = "*/*"

                    putExtra(Intent.EXTRA_TITLE, "relatorio.pdf")
                }

                startActivityForResult(intent, SAVE_PDF)
            } else {
                Snackbar.make(
                    layout_unidade_saude_relatorio,
                    "Sem permissão para salvar arquivos!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun buildField(
        text: String,
        color: Int,
        size: Int,
        gravity: Int,
        caps: Boolean
    ): TextView {
        val tv = TextView(requireContext())

        tv.gravity = gravity

        tv.textSize = size.toFloat()

        tv.setPadding(10, 5, 10, 5)

        tv.setBackgroundColor(color)

        tv.isAllCaps = caps
        tv.text = text

        return tv
    }

    private fun exportPdf(outputStream: OutputStream) {
        val document = PdfDocument()

        // primeira página

//        var pageInfo = PdfDocument.PageInfo.Builder(
//            linear_layout_bar_chart_global.width,
//            linear_layout_bar_chart_global.height,
//            1
//        ).create()
//
//        var page = document.startPage(pageInfo)
//
//        linear_layout_bar_chart_global.draw(page.canvas)

//        document.finishPage(page)

        // segunda página

//        pageInfo = PdfDocument.PageInfo.Builder(
//            linear_layout_relatorio.width,
//            linear_layout_relatorio.height,
//            1
//        ).create()
//
//        page = document.startPage(pageInfo)

//        linear_layout_relatorio.draw(page.canvas)

//        document.finishPage(page)

        // finalizar documento

        document.writeTo(outputStream)

        document.close()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty()) {
            mHavePermissions = if (requestCode == REQUEST_PERMISSION_CODE) {
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            } else {
                Log.e(LOGTAG, "no permission to write to the sdcard!")

                false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        if (requestCode == SAVE_PDF && resultCode == Activity.RESULT_OK) {
            resultData?.data?.also { uri ->
                GlobalScope.launch(Dispatchers.IO) {
                    val os = requireContext().contentResolver.openOutputStream(uri)

                    if (os != null) {
                        exportPdf(os)
                    }
                }

            }
        }
    }

    companion object {
        const val LOGTAG = "Relatorio"
        const val REQUEST_PERMISSION_CODE = 1
        const val SAVE_PDF = 2
    }
}
