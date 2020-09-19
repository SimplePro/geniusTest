package com.wotin.geniustest.Service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.wotin.geniustest.R
import com.wotin.geniustest.getTestModeData
import com.wotin.geniustest.updateTestModeData
import kotlin.concurrent.thread
import kotlin.concurrent.timer

class QuicknessTestHeartManagementService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        lateinit var quicknessInterface : QuicknessTestHeartManagementIsSaved
    }

    fun setQuicknessInterface(quicknessInterfaceParameter: QuicknessTestHeartManagementIsSaved) {
        quicknessInterface = quicknessInterfaceParameter
    }

    interface QuicknessTestHeartManagementIsSaved {
        fun quicknessTestHeartManagement()
    }

    val CHNNEL_ID = "wotin.geniustest"
    val NOTIFICATION_ID = 3

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val chan = NotificationChannel(CHNNEL_ID, "QuicknessForeground", NotificationManager.IMPORTANCE_NONE)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(chan)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val intent = Intent()
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
        intent.putExtra("android.provider.extra.APP_PACKAGE", packageName)
        val pendingIntent = PendingIntent.getActivities(this, 0, arrayOf(intent), PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(this, CHNNEL_ID)
            .setContentText("10 분 후에 순발력 테스트를 할 수 있습니다.")
            .setSubText("클릭하여 알림을 끌 수 있습니다.")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.genius)

        startForeground(NOTIFICATION_ID, notification.build())
        var minutesCount = 11
        timer(period = 1 * 60 * 1000) {
            minutesCount -= 1
            notification.setContentText("$minutesCount 분 후에 순발력 테스트를 할 수 있습니다.")
            startForeground(NOTIFICATION_ID, notification.build())
        }

        Handler().postDelayed({
            runBackground()

            Thread.sleep(500)
            stopSelf()
            stopForeground(true)
            try{
                quicknessInterface.quicknessTestHeartManagement()
            } catch (e : Exception) {
                Log.d("TAG", "onStartCommand: quicknessInterface")
            }

        }, 1 * 60 * 10000)

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