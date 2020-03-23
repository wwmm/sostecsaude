package com.wwmm.sostecsaude.ui.export

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.wwmm.sostecsaude.R
import com.wwmm.sostecsaude.SQLHelper
import kotlinx.android.synthetic.main.fragment_export.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ExportFragment : Fragment() {
    private var mHasPermissions = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions(permissions, REQUEST_PERMISSION_CODE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_export, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val table = MyTable(requireContext(), tablelayout)

        table.build("select * from ${SQLHelper.DB_TABLE_NAME}")

        button_export.setOnClickListener {
            exportPdf()
        }
    }

    private fun exportPdf() {
        if (!mHasPermissions) {
            Snackbar.make(
                export_main_layout, "Não tenho permissão para escrever no cartão!",
                Snackbar.LENGTH_SHORT
            ).show()

            return
        }

        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)

            type = "*/*"

            putExtra(Intent.EXTRA_TITLE, "equipamentos.pdf")
        }

        startActivityForResult(intent, SAVE_PDF)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        if (requestCode == SAVE_PDF && resultCode == Activity.RESULT_OK) {
            resultData?.data?.also { uri ->
                GlobalScope.launch(Dispatchers.IO) {
                    val os = requireContext().contentResolver.openOutputStream(uri)

                    // create a new document
                    val document = PdfDocument()

                    // crate a page description
                    val width = tablelayout.width
                    val height = tablelayout.height

                    val pageInfo = PdfDocument.PageInfo.Builder(
                        width, height,
                        1
                    ).create()

                    // start a page
                    val page = document.startPage(pageInfo)

                    // draw something on the page
                    tablelayout.draw(page.canvas)

                    document.finishPage(page)

                    document.writeTo(os)

                    document.close()

                    os?.close()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty()) {
            mHasPermissions = if (requestCode == REQUEST_PERMISSION_CODE) {
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            } else {
                false
            }
        }
    }

    companion object {
        const val REQUEST_PERMISSION_CODE = 1
        const val SAVE_PDF = 2
    }
}
