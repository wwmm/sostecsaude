package com.wwmm.sostecsaude

import android.util.Log
import androidx.preference.PreferenceManager
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val editor = prefs.edit()

        editor.putString("FBtoken", token)

        editor.apply()

        Log.d(LOGTAG, token)
    }

    companion object {
        const val LOGTAG = "MyFirebaseService"
    }
}