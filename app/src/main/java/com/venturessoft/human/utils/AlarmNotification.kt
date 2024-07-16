package com.venturessoft.human.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.venturessoft.human.views.ui.activities.SplashActivity

class AlarmNotification :BroadcastReceiver() {
    companion object{
        var NOTIFICATION_ID = 0
        var TITLE = ""
        var DESCRIPTION = ""
    }
    override fun onReceive(context: Context, p1: Intent?) {
        createSimpleNotification(context)
    }
    private fun createSimpleNotification(context: Context) {
        val intent = Intent(context, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val flag = PendingIntent.FLAG_IMMUTABLE
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, flag)
        val notification = NotificationCompat.Builder(context, SplashActivity.MY_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_delete)
            .setContentTitle(TITLE)
            .setContentText(DESCRIPTION)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("El periodo de prueba esta por expirar")
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }
}