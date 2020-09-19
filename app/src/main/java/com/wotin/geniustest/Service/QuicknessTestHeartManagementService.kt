package com.wotin.geniustest.Service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.wotin.geniustest.R
import com.wotin.geniustest.Receiver.TestHeartManagementAlarmReceiver
import com.wotin.geniustest.getTestModeData
import com.wotin.geniustest.updateTestModeData
import java.util.*
import kotlin.concurrent.thread

class QuicknessTestHeartManagementService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    val CHNNEL_ID = "QuicknessTestHeartManagementChannelId"
    val NOTIFICATION_ID = 3

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val chan = NotificationChannel(CHNNEL_ID, "QuicknessForeground", NotificationManager.IMPORTANCE_NONE)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(chan)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, CHNNEL_ID)
            .setContentTitle("Quickness Foreground notification")
            .setSmallIcon(R.drawable.genius)
            .build()

        startForeground(NOTIFICATION_ID, notification)

        runBackground()

        Thread.sleep(2000)
        stopSelf()
        stopForeground(true)

        return super.onStartCommand(intent, flags, startId)
    }

    private fun runBackground() {
        thread(start = true) {
            val data = getTestModeData(applicationContext)
            data[2].start = true
            val saveData = data[2]
            updateTestModeData(applicationContext, saveData)
            Log.d("TAG", "onStartCommand: updated data (quickness service)")
        }
    }

}