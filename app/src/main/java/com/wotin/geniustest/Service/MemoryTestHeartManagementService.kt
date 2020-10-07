package com.wotin.geniustest.Service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.wotin.geniustest.R
import com.wotin.geniustest.Receiver.TestHeartManagementReceiver
import com.wotin.geniustest.RoomMethod.GetRoomMethod
import com.wotin.geniustest.RoomMethod.UpdateRoomMethod
import java.lang.Exception
import kotlin.concurrent.thread

class MemoryTestHeartManagementService : Service(), TestHeartManagementReceiver.MemoryTestHeartManagementInterface {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    val CHANNEL_ID = "wotin.geniustest"
    val NOTIFICATION_ID = 1

    companion object {
        lateinit var memoryInterface : memoryTestHeartManagementIsSaved
    }

    fun setMemoryInterface(memoryInterfaceParameter: memoryTestHeartManagementIsSaved) {
        memoryInterface = memoryInterfaceParameter
    }

    interface memoryTestHeartManagementIsSaved {
        fun memoryTestHeartManagement()
    }

    private fun createNotificationChannel() {
        val chan = NotificationChannel(CHANNEL_ID, "memoryForeground", NotificationManager.IMPORTANCE_NONE)
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
        receiver.setMemoryTestHeartManagement(this)
        receiver.setContext(applicationContext)

        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentText("10분이 지나면 알람을 꺼집니다. (기억력 테스트)")
            .setSubText("클릭하여 알림을 끌 수 있습니다.")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.genius)

        startForeground(NOTIFICATION_ID, notification.build())

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, TestHeartManagementReceiver::class.java)
        alarmIntent.putExtra("test", "memory")
        val alarmPendingIntent = PendingIntent.getBroadcast(this, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1 * 60 * 10000), alarmPendingIntent)


        return super.onStartCommand(intent, flags, startId)
    }

    private fun runBackground() {
        Log.d("TAG", "runBackground memory")
        thread(start = true) {
            val data = GetRoomMethod().getTestModeData(applicationContext)
            data[0].start = true
            val saveData = data[0]
            UpdateRoomMethod().updateTestModeData(applicationContext, saveData)
            Log.d("TAG", "onStartCommand: updated data (memory service)")
        }
    }

    override fun testMemoryHeartManagement() {
        runBackground()
        Thread.sleep(500)
        stopSelf()
        stopForeground(true)

        Log.d("TAG", "testMemoryHeartManagement")

        try {
            memoryInterface.memoryTestHeartManagement()
        } catch (e: Exception) {
            Log.d("TAG", "onStartCommand: memoryInterface")
        }
    }
}