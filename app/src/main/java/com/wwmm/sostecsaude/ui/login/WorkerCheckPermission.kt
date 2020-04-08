package com.wwmm.sostecsaude.ui.login

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.wwmm.sostecsaude.MainActivity
import com.wwmm.sostecsaude.R
import com.wwmm.sostecsaude.myServerURL

class WorkerCheckPermission(private val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val prefs = getDefaultSharedPreferences(appContext)

        val queue = Volley.newRequestQueue(appContext)

        val request = object : StringRequest(
            Method.POST, "$myServerURL/check_write_permission",
            Response.Listener { response ->
                val msg = response.toString()

                Log.d(LOGTAG, msg)

                if (msg == "has_write_permission") {
                    WorkManager.getInstance(appContext).cancelUniqueWork(
                        appContext.getString(
                            R.string.notification_check_permission_id
                        )
                    )

                    createNotification()
                }
            },
            Response.ErrorListener {
                Log.d(LOGTAG, "failed request: $it")
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()

                params["token"] = prefs.getString("Token", "")!!

                return params
            }
        }

        queue.add(request)

        return Result.success()
    }

    private fun createNotification() {
        val id = appContext.getString(R.string.notification_check_permission_id)

        val intent = Intent(appContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            appContext, 0,
            intent, 0
        )

        val builder = NotificationCompat.Builder(appContext, id)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Seu cadastro foi validado")
            .setContentText("Agora você tem acesso a todas as funcionalidades do aplicativo")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(appContext)) {
            // notificationId is a unique int for each notification that you must define

            notify(1, builder.build())
        }
    }

    companion object {
        const val LOGTAG = "worker check permission"
    }
}