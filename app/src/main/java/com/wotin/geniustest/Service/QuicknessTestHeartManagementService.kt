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
import com.wotin.geniustest.Receiver.TestHeartManagementReceiver
import com.wotin.geniustest.RoomMethod.GetRoomMethod
import com.wotin.geniustest.RoomMethod.UpdateRoomMethod

class QuicknessTestHeartManagementService : Service(), TestHeartManagementReceiver.QuicknessTestHeartManagementInterface {
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
            .setContentText("10분이 지나면 알람을 꺼집니다. (순발력 테스트)")
            .setSubText("클릭하여 알림을 끌 수 있습니다.")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.genius)

        val receiver = TestHeartManagementReceiver()
        receiver.setQuicknessTestHeartManagement(this)
        receiver.setContext(applicationContext)

        startForeground(NOTIFICATION_ID, notification.build())

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, TestHeartManagementReceiver::class.java)
        alarmIntent.putExtra("test", "quickness")
        val alarmPendingIntent = PendingIntent.getBroadcast(this, 3, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1 * 60 * 1000), alarmPendingIntent)

        return super.onStartCommand(intent, flags, startId)
    }

    private fun runBackground() {
        Log.d("TAG", "runBackground quickness")
        val data = GetRoomMethod().getTestModeData(applicationContext)
        data[2].start = true
        val saveData = data[2]
        UpdateRoomMethod().updateTestModeData(applicationContext, saveData)
        Log.d("TAG", "onStartCommand: updated data (quickness service)")
    }

    override fun testQuicknessHeartManagement() {
        Log.d("TAG", "testQuicknessHeartManagement")
        Thread.sleep(500)
        runBackground()
        stopSelf()
        stopForeground(true)
        try{
            quicknessInterface.quicknessTestHeartManagement()
        } catch (e : Exception) {
            Log.d("TAG", "onStartCommand: quicknessInterface")
        }
    }

}