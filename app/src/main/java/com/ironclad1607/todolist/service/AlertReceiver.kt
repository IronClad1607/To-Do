package com.ironclad1607.todolist.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationHelper = NotificationHelper(context!!)
        val nb = notificationHelper.channelNotificationManager
        notificationHelper.mManager.notify(1, nb.build())
    }

}