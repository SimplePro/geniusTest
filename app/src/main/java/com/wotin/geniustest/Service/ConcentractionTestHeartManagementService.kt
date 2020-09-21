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
import com.wotin.geniustest.Receiver.TestHeartManagementReceiver
import com.wotin.geniustest.getTestModeData
import com.wotin.geniustest.updateTestModeData
import java.lang.Exception
import java.util.*
import kotlin.concurrent.thread
import kotlin.concurrent.timer

class ConcentractionTestHeartManagementService : Service(), TestHeartManagementReceiver.ConcentractionTestHeartManagementInterface {
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

        val receiver = TestHeartManagementReceiver()
        receiver.setConcentractionTestHeartManagement(this)
        receiver.setContext(applicationContext)

        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentText("10분이 지나면 알람을 꺼집니다. (집중력 테스트)")
            .setSubText("클릭하여 알림을 끌 수 있습니다.")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.genius)

        startForeground(NOTIFICATION_ID, notification.build())

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, TestHeartManagementReceiver::class.java)
        alarmIntent.putExtra("test", "concentraction")
        val alarmPendingIntent = PendingIntent.getBroadcast(this, 2, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1 * 60 * 1000), alarmPendingIntent)


        return super.onStartCommand(intent, flags, startId)
    }

    private fun runBackground() {
        Log.d("TAG", "runBackground concentraction")
        thread(start = true) {
            val data = getTestModeData(applicationContext)
            data[1].start = true
            val saveData = data[1]
            updateTestModeData(applicationContext, saveData)
            Log.d("TAG", "onStartCommand: updated data (concentraction service)")
        }
    }

    override fun testCocentractionHeartManagement() {
        runBackground()
        Thread.sleep(500)
        stopSelf()
        stopForeground(true)
        
        Log.d("TAG", "testCocentractionHeartManagement")

        try{
            concentractionInterface.concentractionTestHeartManagement()
        } catch (e : Exception) {
            Log.d("TAG", "onStartCommand: quicknessInterface")
        }
    }

}

