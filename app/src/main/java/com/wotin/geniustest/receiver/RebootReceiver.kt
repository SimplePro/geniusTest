package com.wotin.geniustest.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.wotin.geniustest.customClass.UserCustomClass
import com.wotin.geniustest.database.UserDB
import com.wotin.geniustest.service.RebootTestHeartManagementService

class RebootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            val userData = getUserData(context)
            if (userData != null) {
                if ("android.intent.action.BOOT_COMPLETED" == intent!!.action || Intent.ACTION_BOOT_COMPLETED == intent.action) {
                    val service = Intent(context, RebootTestHeartManagementService::class.java)
                    ContextCompat.startForegroundService(context!!.applicationContext, service)
                }
            } else {
                Log.d("TAG", "RebootReceiver : onReceive userData is null")
            }
        } catch (e : Exception) {
            Log.d("TAG", "RebootReceiver : onReceive userData is catch")
        }

    }


    private fun getUserData(context: Context?) : UserCustomClass {
        val userDB : UserDB = Room.databaseBuilder(
            context!!.applicationContext,
            UserDB::class.java, "user.db"
        ).allowMainThreadQueries()
            .build()
        val userData : UserCustomClass = userDB.userDB().getAll()
        return userData
    }
}