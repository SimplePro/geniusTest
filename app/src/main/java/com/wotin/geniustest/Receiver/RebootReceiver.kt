package com.wotin.geniustest.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.wotin.geniustest.Service.RebootTestHeartManagementService

class RebootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if ("android.intent.action.BOOT_COMPLETED" == intent!!.action || Intent.ACTION_BOOT_COMPLETED == intent.action) {
            val service = Intent(context, RebootTestHeartManagementService::class.java)
            ContextCompat.startForegroundService(context!!.applicationContext, service)
        }
    }
}