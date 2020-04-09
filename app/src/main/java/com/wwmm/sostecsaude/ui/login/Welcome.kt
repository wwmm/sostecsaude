package com.wwmm.sostecsaude.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.work.*
import com.wwmm.sostecsaude.R
import kotlinx.android.synthetic.main.fragment_welcome.*
import java.util.concurrent.TimeUnit

class Welcome : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controller = findNavController()

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val perfil = prefs.getString("Perfil", "")!!

        if (perfil == "unidade_manutencao") {
            textView_welcome.setText(R.string.title_welcome_unidade_manutencao)
        }

        val editor = prefs.edit()

        editor.putBoolean("CriandoConta", false)

        editor.apply()

        val constraint = Constraints.Builder().setRequiredNetworkType(
            NetworkType.CONNECTED).build()

        val worker = PeriodicWorkRequestBuilder<WorkerCheckPermission>(
            15, TimeUnit.MINUTES
        ).setConstraints(constraint).build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            getString(R.string.notification_check_permission_id),
            ExistingPeriodicWorkPolicy.REPLACE,
            worker
        )

        button_continuar.setOnClickListener {
            when (perfil) {
                "unidade_saude" -> {
                    controller.navigate(R.id.action_welcome_to_unidadeSaude)
                }

                "unidade_manutencao" -> {
                    controller.navigate(R.id.action_welcome_to_unidadeManutencao)
                }
            }
        }
    }
}
