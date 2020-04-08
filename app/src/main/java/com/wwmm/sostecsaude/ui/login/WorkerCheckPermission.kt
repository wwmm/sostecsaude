package com.wwmm.sostecsaude.ui.login

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.wwmm.sostecsaude.MainActivity
import com.wwmm.sostecsaude.R
import com.wwmm.sostecsaude.myServerURL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


class WorkerCheckPermission(private val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val prefs = getDefaultSharedPreferences(appContext)

        val queue = Volley.newRequestQueue(appContext)

        val future = RequestFuture.newFuture<String>()

        val request = object : StringRequest(
            Method.POST, "$myServerURL/check_write_permission", future, future
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()

                params["token"] = prefs.getString("Token", "")!!

                return params
            }
        }

        queue.add(request)

        val msg = future.get(10, TimeUnit.SECONDS)

        if (msg == "has_write_permission") {
            createNotification()

            WorkManager.getInstance(appContext).cancelUniqueWork(
                appContext.getString(R.string.notification_check_permission_id)
            )
        } else {
            Log.d(LOGTAG, msg)
        }

        Result.success()
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