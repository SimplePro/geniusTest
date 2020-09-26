package com.wotin.geniustest.Service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.wotin.geniustest.R
import com.wotin.geniustest.RoomMethod.GetRoomMethod
import com.wotin.geniustest.RoomMethod.UpdateRoomMethod
import kotlin.concurrent.thread

class RebootTestHeartManagementService : Service() {

    val CHANNEL_ID = "wotin.geniustest"
    val NOTIFICATION_ID = 4

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        val chan = NotificationChannel(CHANNEL_ID, "RebootForeground", NotificationManager.IMPORTANCE_NONE)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(chan)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentText("앱 설정중...")
            .setSmallIcon(R.drawable.genius)

        startForeground(NOTIFICATION_ID, notification.build())

        Log.d("TAG", "RebootService : onStartCommand")
        Thread.sleep(10000)

        updateDataConcentraction(applicationContext)
        updateDataQuickness(applicationContext)
        stopSelf()
        stopForeground(true)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateDataConcentraction(context: Context) {
        Log.d("TAG", "runBackground concentraction")
        thread(start = true) {
            val data = GetRoomMethod().getTestModeData(context = context)
            data[1].start = true
            val saveData = data[1]
            UpdateRoomMethod().updateTestModeData(context, saveData)
            Log.d("TAG", "onStartCommand: updated data (concentraction service)")
        }
    }

    private fun updateDataQuickness(context: Context) {
        Log.d("TAG", "runBackground quickness")
        val data = GetRoomMethod().getTestModeData(context)
        data[2].start = true
        val saveData = data[2]
        UpdateRoomMethod().updateTestModeData(context, saveData)
        Log.d("TAG", "onStartCommand: updated data (quickness service)")
    }


}