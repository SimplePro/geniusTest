package com.wotin.geniustest.Service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.wotin.geniustest.R
import com.wotin.geniustest.Receiver.TestHeartManagementAlarmReceiver
import com.wotin.geniustest.getTestModeData
import com.wotin.geniustest.updateTestModeData
import java.util.*
import kotlin.concurrent.thread

class ConcentractionTestHeartManagementService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    val CHANNEL_ID = "ConcentractionTestHeartManagementChannelId"
    val NOTIFICATION_ID = 2


    private fun createNotificationChannel() {
        val chan = NotificationChannel(CHANNEL_ID, "ConcentractionForeground", NotificationManager.IMPORTANCE_NONE)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(chan)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
        notification
            .setContentTitle("Concentraction Foreground notification")
            .setSmallIcon(R.drawable.genius)

        startForeground(NOTIFICATION_ID, notification.build())

        runBackground()

        Thread.sleep(2000)
        stopSelf()
        stopForeground(true)

        return super.onStartCommand(intent, flags, startId)
    }

    private fun runBackground() {
        thread(start = true) {
            val data = getTestModeData(applicationContext)
            data[1].start = true
            val saveData = data[1]
            updateTestModeData(applicationContext, saveData)
            Log.d("TAG", "onStartCommand: updated data (concentraction service)")
        }
    }

}

