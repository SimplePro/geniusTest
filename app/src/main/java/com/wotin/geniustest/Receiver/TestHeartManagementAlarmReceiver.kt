package com.wotin.geniustest.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.room.Room
import com.wotin.geniustest.DB.Genius.GeniusTestDataDB

class TestHeartManagementAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent!!.hasExtra("test")) {
            val test = intent.getStringExtra("test")!!
        }
    }
}