package com.wotin.geniustest.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.wotin.geniustest.Service.ConcentractionTestHeartManagementService
import com.wotin.geniustest.Service.QuicknessTestHeartManagementService
import com.wotin.geniustest.getTestModeData
import com.wotin.geniustest.updateTestModeData
import java.lang.Exception

class TestHeartManagementAlarmReceiver : BroadcastReceiver() {

    companion object {
        lateinit var testHeartManagement : TestHeartManagementInterface
    }


    fun setTestHeartManagement(testHeartManagementParameter: TestHeartManagementInterface) {
        testHeartManagement = testHeartManagementParameter
    }

    interface TestHeartManagementInterface {
        fun testHeartManagement()
    }


    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent!!.hasExtra("test")) {
            Log.d("TAG", "onReceive: receive")
            when (intent.getStringExtra("test")!!) {
                "memory" -> {
                    val data = getTestModeData(context!!.applicationContext)
                    data[0].start = true
                    val saveData = data[0]
                    updateTestModeData(context.applicationContext, saveData)
                }
                "concentraction" -> {
                    ContextCompat.startForegroundService(context!!, Intent(context.applicationContext, ConcentractionTestHeartManagementService::class.java))
                }
                "quickness" -> {
                    ContextCompat.startForegroundService(context!!, Intent(context.applicationContext, QuicknessTestHeartManagementService::class.java))
                }
            }
            try {
                testHeartManagement.testHeartManagement()
            } catch (e : Exception) {
                Log.d("TAG", "onReceive: catch testHeartManagement")
            }
        }
    }

}

