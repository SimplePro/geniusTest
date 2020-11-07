package com.wotin.geniustest.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class TestHeartManagementReceiver: BroadcastReceiver() {


    companion object {
        lateinit var concentractionTestHeartManagement: ConcentractionTestHeartManagementInterface
        lateinit var quicknessTestHeartManagement: QuicknessTestHeartManagementInterface
        lateinit var memoryTestHeartManagement: MemoryTestHeartManagementInterface
        lateinit var context: Context
    }

    fun setMemoryTestHeartManagement(memoryTestHeartManagementParameter: MemoryTestHeartManagementInterface) {
        memoryTestHeartManagement = memoryTestHeartManagementParameter
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

    interface MemoryTestHeartManagementInterface {
        fun testMemoryHeartManagement()
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
                "memory" -> {
                    Log.d("TAG", "intent.hasExtra('test') and test is memory")
                    memoryTestHeartManagement.testMemoryHeartManagement()
                }
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