package com.ironclad1607.todolist.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ironclad1607.todolist.R

class NotificationHelper(base: Context) : ContextWrapper(base) {

    val mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mManager.createNotificationChannel(
                NotificationChannel(
                    "channedID",
                    "Agenda",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
    }

    val channelNotificationManager = NotificationCompat.Builder(applicationContext, "channelID")
        .setContentTitle("Test!")
        .setContentText("Test Notification")
        .setSmallIcon(R.mipmap.logo)!!

}