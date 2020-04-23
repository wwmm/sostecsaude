package com.wwmm.sostecsaude.ui.unidade_manutencao

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.preference.PreferenceManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.wwmm.sostecsaude.R
import com.wwmm.sostecsaude.connectionErrorMessage
import com.wwmm.sostecsaude.myServerURL
import kotlinx.android.synthetic.main.fragment_unidade_manutencao_relatorio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStream
import java.nio.charset.Charset

class Relatorio : Fragment() {
    private lateinit var mController: NavController
    private lateinit var mQueue: RequestQueue
    private lateinit var mPrefs: SharedPreferences
    private var mProcessing = false
    private val mClientNameList = ArrayList<String>()
    private var mMapClienteEquipamentos = mutableMapOf<String, JSONArray>()
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
        return inflater.inflate(R.layout.fragment_unidade_manutencao_relatorio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        mQueue = Volley.newRequestQueue(requireContext())

        criarRelatorio()

        button_gerar_relatorio.setOnClickListener {
            criarRelatorio()
        }

        button_salvar_relatorio.setOnClickListener {
            if (mHavePermissions) {
                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)

                    type = "*/*"

                    putExtra(Intent.EXTRA_TITLE, "relatorio.pdf")
                }

                startActivityForResult(intent, SAVE_PDF)
            }else{
                Snackbar.make(
                    layout_manutencao_relatorio,
                    "Sem permissão para salvar arquivos!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun criarRelatorio() {
        if (!mProcessing) {
            table_layout_status_global.removeAllViews()

            getClients()
        }
    }

    private fun getClients() {
        progressBar.visibility = View.VISIBLE
        mProcessing = true

        val token = mPrefs.getString("Token", "")!!

        val jsonToken = JSONArray()

        jsonToken.put(0, token)

        mClientNameList.clear()

        val request = JsonArrayRequest(
            Request.Method.POST,
            "$myServerURL/unidade_manutencao_lista_clientes",
            jsonToken,
            Response.Listener { response ->
                if (isAdded) {
                    if (response.length() > 0) {
                        if (response[0] == "invalid_token" || response[0] == "perfil_invalido") {
                            mController.navigate(R.id.action_global_fazerLogin)
                        } else {
                            mMapClienteEquipamentos.clear()

                            for (n in 0 until response.length()) {
                                val unidade = response[n] as JSONObject
                                val nome = unidade["nome"] as String
                                val emailCliente = unidade["email"] as String

                                mClientNameList.add(nome)

                                getEquipamentos(nome, emailCliente)
                            }
                        }
                    }

                    progressBar.visibility = View.GONE
                    mProcessing = false
                }
            },
            Response.ErrorListener {
                Log.d(LOGTAG, "failed request: $it")

                if (isAdded) {
                    progressBar.visibility = View.GONE
                }

                mProcessing = false

                connectionErrorMessage(layout_manutencao_relatorio, it)
            }
        )

        mQueue.add(request)
    }

    private fun getEquipamentos(nomeCliente: String, emailCliente: String) {
        progressBar.visibility = View.VISIBLE
        mProcessing = true

        val token = mPrefs.getString("Token", "")!!
        val jsonToken = JSONArray()

        jsonToken.put(0, token)
        jsonToken.put(1, emailCliente)

        val request = JsonArrayRequest(
            Request.Method.POST,
            "$myServerURL/lista_equipamentos_cliente",
            jsonToken,
            Response.Listener { response ->
                if (isAdded) {
                    if (response.length() > 0 && response[0] == "invalid_token") {
                        mController.navigate(R.id.action_global_fazerLogin)
                    } else {
                        mMapClienteEquipamentos[nomeCliente] = response

                        if (mMapClienteEquipamentos.size == mClientNameList.size) {
                            createGlobalTable()
                        }

                        progressBar.visibility = View.GONE
                        mProcessing = false
                    }
                }
            },
            Response.ErrorListener {
                Log.d(LOGTAG, "failed request: $it")

                if (isAdded) {
                    progressBar.visibility = View.GONE
                }

                mProcessing = false

                connectionErrorMessage(layout_manutencao_relatorio, it)
            }
        )

        mQueue.add(request)
    }

    private fun createGlobalTable() {
        val layoutParams = TableLayout.LayoutParams()

        layoutParams.setMargins(0, 5, 0, 5)

        // column headers

        var row = TableRow(requireContext())

        row.layoutParams = layoutParams

        row.addView(buildField("unidade", Color.WHITE, 14, Gravity.CENTER, false))
        row.addView(buildField("aguardando aceite", Color.LTGRAY, 14, Gravity.CENTER, false))
        row.addView(buildField("aceito", Color.GRAY, 14, Gravity.CENTER, false))
        row.addView(buildField("pronto para retirada", Color.LTGRAY, 14, Gravity.CENTER, false))
        row.addView(buildField("retirado", Color.GRAY, 14, Gravity.CENTER, false))
        row.addView(buildField("recebido", Color.LTGRAY, 14, Gravity.CENTER, false))
        row.addView(buildField("triagem", Color.GRAY, 14, Gravity.CENTER, false))
        row.addView(buildField("manutenção", Color.LTGRAY, 14, Gravity.CENTER, false))
        row.addView(buildField("higienização", Color.GRAY, 14, Gravity.CENTER, false))
        row.addView(buildField("saiu para entrega", Color.LTGRAY, 14, Gravity.CENTER, false))
        row.addView(buildField("recebido", Color.GRAY, 14, Gravity.CENTER, false))

        row.gravity = Gravity.START

        table_layout_status_global.addView(row)

        for (unidadeNome in mClientNameList) {
            val listaEquipamentos = mMapClienteEquipamentos[unidadeNome] as JSONArray

            val countState = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

            for (n in 0 until listaEquipamentos.length()) {
                val json = listaEquipamentos[n] as JSONObject

                countState[json.getInt("estado")]++
            }

            if (listaEquipamentos.length() == 0) {
                countState[0]++
            }

            row = TableRow(requireContext())

            row.layoutParams = layoutParams
            row.gravity = Gravity.START

            row.addView(buildField(unidadeNome, Color.WHITE, 14, Gravity.CENTER, false))

            for (n in 0..9) {
                row.addView(
                    buildField(
                        countState[n].toString(),
                        Color.WHITE,
                        14,
                        Gravity.CENTER,
                        false
                    )
                )
            }

            table_layout_status_global.addView(row)
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
        // create a new document
        val document = PdfDocument()

        // crate a page description
        val width = linear_layout_relatorio.width
        val height = linear_layout_relatorio.height

        val pageInfo = PdfDocument.PageInfo.Builder(width, height, 1).create()

        // start a page
        val page = document.startPage(pageInfo)

        // draw something on the page
        linear_layout_relatorio.draw(page.canvas)

        document.finishPage(page)

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

                    if(os != null){
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
