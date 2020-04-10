package com.wwmm.sostecsaude

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val editor = prefs.edit()

        editor.putString("FBtoken", token)

        editor.apply()

        Log.d(LOGTAG, token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.data["Title"]
        val body = remoteMessage.data["Body"]

        if (title != null && body != null) {
            showNotification(title, body)
        }
    }

    private fun showNotification(title: String, body: String) {
        val manager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as
                NotificationManager

        val channelId = getString(R.string.notification_avisos_id)

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0,
            intent, 0)

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext,
            channelId
        )
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        manager.notify(0, builder.build())
    }

    companion object {
        const val LOGTAG = "MyFirebaseService"
    }
}