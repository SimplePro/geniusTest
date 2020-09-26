package com.wotin.geniustest.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class TestHeartManagementReceiver: BroadcastReceiver() {


    companion object {
        lateinit var concentractionTestHeartManagement: ConcentractionTestHeartManagementInterface
        lateinit var quicknessTestHeartManagement: QuicknessTestHeartManagementInterface
        lateinit var context: Context
    }

    fun setConcentractionTestHeartManagement(concentractionTestHeartManagementParameter: ConcentractionTestHeartManagementInterface) {
        concentractionTestHeartManagement = concentractionTestHeartManagementParameter
    }

    fun setQuicknessTestHeartManagement(quicknessTestHeartManagementParameter: QuicknessTestHeartManagementInterface) {
        quicknessTestHeartManagement = quicknessTestHeartManagementParameter
    }

    fun setContext(contextParameter : Context) {
        context = contextParameter
    }

    interface ConcentractionTestHeartManagementInterface {
        fun testCocentractionHeartManagement()
    }

    interface QuicknessTestHeartManagementInterface {
        fun testQuicknessHeartManagement()
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent!!.hasExtra("test")) {
            when(intent!!.getStringExtra("test")) {
                "concentraction" -> {
                    Log.d("TAG", "intent.hasExtra('test') and test is concentraction")
                    concentractionTestHeartManagement.testCocentractionHeartManagement()
                }
                "quickness" -> {
                    Log.d("TAG", "intent.hasExtra('test') and test is quickness")
                    quicknessTestHeartManagement.testQuicknessHeartManagement()
                }
            }
        }
    }
}