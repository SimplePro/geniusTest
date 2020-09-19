package com.wotin.geniustest.Service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.wotin.geniustest.Activity.MainActivity
import com.wotin.geniustest.Activity.Test.TestActivity
import com.wotin.geniustest.R
import com.wotin.geniustest.getTestModeData
import com.wotin.geniustest.updateTestModeData
import java.lang.Exception
import java.util.*
import kotlin.concurrent.thread
import kotlin.concurrent.timer

class ConcentractionTestHeartManagementService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    val CHANNEL_ID = "wotin.geniustest"
    val NOTIFICATION_ID = 2

    companion object {
        lateinit var concentractionInterface : ConcentractionTestHeartManagementIsSaved
    }

    fun setConcentractionInterface(concentractionInterfaceParameter: ConcentractionTestHeartManagementIsSaved) {
        concentractionInterface = concentractionInterfaceParameter
    }

    interface ConcentractionTestHeartManagementIsSaved {
        fun concentractionTestHeartManagement()
    }

    private fun createNotificationChannel() {
        val chan = NotificationChannel(CHANNEL_ID, "ConcentractionForeground", NotificationManager.IMPORTANCE_NONE)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(chan)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val intent = Intent()
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
        intent.putExtra("android.provider.extra.APP_PACKAGE", packageName)
        val pendingIntent = PendingIntent.getActivities(this, 0, arrayOf(intent), PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentText("10 분 후에 집중력 테스트를 할 수 있습니다.")
            .setSubText("클릭하여 알림을 끌 수 있습니다.")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.genius)

        startForeground(NOTIFICATION_ID, notification.build())

        var minutesCount = 11

        timer(period = 1 * 60 * 1000) {
            minutesCount -= 1
            notification.setContentText("$minutesCount 분 후에 집중력 테스트를 할 수 있습니다.")
            startForeground(NOTIFICATION_ID, notification.build())
        }

        Handler().postDelayed({
            runBackground()
            Thread.sleep(500)
            stopSelf()
            stopForeground(true)

            try{
                concentractionInterface.concentractionTestHeartManagement()
            } catch (e : Exception) {
                Log.d("TAG", "onStartCommand: quicknessInterface")
            }

        }, 1 * 60 * 10000)


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

