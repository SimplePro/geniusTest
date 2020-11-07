package com.wotin.geniustest.activity.userManagement

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.http.HttpResponseCache
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.room.Room
import com.wotin.geniustest.activity.loginAndSignUp.LoginActivity
import com.wotin.geniustest.activity.MainActivity
import com.wotin.geniustest.database.UserDB
import com.wotin.geniustest.EncryptionAndDetoxification
import com.wotin.geniustest.R
import com.wotin.geniustest.receiver.TestHeartManagementReceiver
import com.wotin.geniustest.retrofitBuilder.GeniusRetrofitBuilder
import com.wotin.geniustest.roomMethod.DeleteRoomMethod
import com.wotin.geniustest.service.ConcentractionTestHeartManagementService
import com.wotin.geniustest.service.MemoryTestHeartManagementService
import com.wotin.geniustest.service.QuicknessTestHeartManagementService
import kotlinx.android.synthetic.main.activity_delete_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class DeleteUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_user)

        val userDB : UserDB = Room.databaseBuilder(
            applicationContext,
            UserDB::class.java, "user.db"
        ).allowMainThreadQueries()
            .build()
        val userData = userDB.userDB().getAll()

        go_to_mainactivity_from_delete_account_imageview.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        delete_account_confirm_button.setOnClickListener {
            if(check_password_before_delete_account_edittext.text.toString() == EncryptionAndDetoxification().encryptionAndDetoxification(userData.password)) {
                GeniusRetrofitBuilder.deleteAccountApiService.deleteAccountAndData(userData.UniqueId).enqueue(object : Callback<HttpResponseCache> {
                    override fun onFailure(call: Call<HttpResponseCache>, t: Throwable) {
                        Toast.makeText(applicationContext, "계정이 삭제되지 않았습니다.", Toast.LENGTH_LONG).show()
                    }
                    override fun onResponse(
                        call: Call<HttpResponseCache>,
                        response: Response<HttpResponseCache>
                    ) {
                        if(response.code() == 204) {
                            Toast.makeText(applicationContext, "성공적으로 계정이 삭제되었습니다", Toast.LENGTH_LONG).show()
                            DeleteRoomMethod().deleteUserDataAndGeniusTestAndPracticeData(applicationContext)
                            Toast.makeText(applicationContext, "${userData.name} 님 그동안 '천재 테스트' 를 즐겨주셔서 감사합니다.", Toast.LENGTH_LONG).show()
                            deleteUserDataSharedPreference()
                            DeleteRoomMethod().deleteTestModeData(applicationContext)
                            stopService(Intent(this@DeleteUserActivity, MemoryTestHeartManagementService::class.java))
                            stopService(Intent(this@DeleteUserActivity, ConcentractionTestHeartManagementService::class.java))
                            stopService(Intent(this@DeleteUserActivity, QuicknessTestHeartManagementService::class.java))
                            cancelAlarm()
                            val intent = Intent(this@DeleteUserActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(applicationContext, "계정이 삭제되지 않았습니다.", Toast.LENGTH_LONG).show()
                        }
                    }
                })
            } else {
                Toast.makeText(applicationContext, "비밀번호가 맞지 않습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun deleteUserDataSharedPreference() {
        val pref = getPreferences(0)
        val editor = pref.edit()

        editor
            .remove("UID")
            .remove("id")
            .remove("password")
            .apply()
    }

    private fun cancelAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, TestHeartManagementReceiver::class.java)
        try {
            val memoryPendingIntent = PendingIntent.getBroadcast(this, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.cancel(memoryPendingIntent)
            memoryPendingIntent.cancel()
        } catch (e : Exception) {

        }
        try {
            val concentractionPendingIntent = PendingIntent.getBroadcast(this, 2, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.cancel(concentractionPendingIntent)
            concentractionPendingIntent.cancel()
        } catch (e : Exception) {

        }

        try {
            val quicknessPendingIntent = PendingIntent.getBroadcast(this, 3, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.cancel(quicknessPendingIntent)
            quicknessPendingIntent.cancel()
        } catch (e : Exception) {

        }
    }

}