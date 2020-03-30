package com.wwmm.sostecsaude.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wwmm.sostecsaude.MainActivity
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.Database

class WebAppInterface(
    private val mContext: Context, private val controller: NavController,
    private val bottomNav: BottomNavigationView
) {

    @JavascriptInterface
    fun showToast(toast: String) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun credentials(msg: String) {
        if(msg.isBlank()){
            return
        }

        val tmp = msg.split("<&>")

        if (tmp.size > 1) {
            val perfil = tmp[0]
            val login = tmp[1]
            val senha = tmp[2]
            val email = tmp[3]

            Database.connect(
                "jdbc:mysql://albali.eic.cefet-rj.br/sostecsaude",
                driver = "com.mysql.jdbc.Driver",
                user = login,
                password = senha
            )

            when (perfil) {
                "unidade_saude" -> {
                    val prefs = mContext.getSharedPreferences(
                        "UnidadeSaude",
                        0
                    )

                    val editor = prefs.edit()

                    editor.putString("Email", email)

                    editor.apply()

                    loadUnidadeSaude()
                }

                "unidade_manutencao" -> {
                    val prefs = mContext.getSharedPreferences(
                        "UnidadeManutencao",
                        0
                    )

                    val editor = prefs.edit()

                    editor.putString("Email", email)

                    editor.apply()

                    loadUnidadeManutencao()
                }
            }
        } else {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadUnidadeSaude() {
        GlobalScope.launch(Dispatchers.Main) {
            bottomNav.inflateMenu(R.menu.menu_bottom_nav_relatar)

            bottomNav.visibility = View.VISIBLE

            bottomNav.setOnNavigationItemSelectedListener(null)

            bottomNav.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_bottomnav_relatar_danos_add -> {
                        controller.navigate(R.id.action_global_addFragment)
                    }

                    R.id.menu_bottomnav_relatar_danos_submissoes -> {
                        controller.navigate(R.id.action_global_listFragment)
                    }

                    R.id.menu_bottomnav_relatar_danos_oficinas -> {
                        controller.navigate(R.id.action_global_verOficinas)
                    }

                    R.id.menu_bottomnav_relatar_danos_contato -> {
                        controller.navigate(R.id.action_global_unidadeSaude)
                    }
                }

                true
            }

            controller.navigate(R.id.action_login_to_nested_graph_relatar)
        }
    }

    private fun loadUnidadeManutencao() {
        GlobalScope.launch(Dispatchers.Main) {
            bottomNav.inflateMenu(R.menu.menu_bottom_nav_unidade_manutencao)

            bottomNav.visibility = View.VISIBLE

            bottomNav.setOnNavigationItemSelectedListener(null)

            bottomNav.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_bottomnav_unidade_manutencao_pedidos -> {
                        controller.navigate(R.id.action_global_empresasVerPedidos)
                    }

                    R.id.menu_bottomnav_unidade_manutencao_contato -> {
                        controller.navigate(R.id.action_global_unidadeManutencao)
                    }
                }

                true
            }

            controller.navigate(R.id.action_login_to_nested_graph_unidade_manutencao)
        }
    }
}

class Login : Fragment() {
    private lateinit var mCookieManager: CookieManager

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val activity = context as MainActivity

        activity.mLogin = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controller = findNavController()

        val bottomNav = requireActivity().findViewById(R.id.bottom_nav) as
                BottomNavigationView

        webview.settings.javaScriptEnabled = true
        webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        webview.addJavascriptInterface(
            WebAppInterface(requireContext(), controller, bottomNav),
            "Android"
        )

        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return false
            }
        }

        mCookieManager = CookieManager.getInstance()

        mCookieManager.setAcceptCookie(true)
        mCookieManager.setAcceptThirdPartyCookies(webview, true)

        webview.loadUrl("http://albali.eic.cefet-rj.br:8081")
    }

    override fun onPause() {
        super.onPause()

        mCookieManager.flush()
    }

    fun goBack() {
        if (isAdded) {
            if (webview.canGoBack()) {
                webview.goBack()
            }
        }
    }
}
