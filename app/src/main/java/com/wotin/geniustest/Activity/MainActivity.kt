package com.wotin.geniustest.Activity

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonObject
import com.wotin.geniustest.*
import com.wotin.geniustest.Activity.LoginAndSignUp.LoginActivity
import com.wotin.geniustest.Activity.UserManagement.DeleteUserActivity
import com.wotin.geniustest.Activity.UserManagement.UserInformationActivity
import com.wotin.geniustest.Adapter.TabLayoutFragmentPagerAdapter
import com.wotin.geniustest.Fragment.PracticeFragment
import com.wotin.geniustest.Receiver.TestHeartManagementReceiver
import com.wotin.geniustest.RetrofitInterface.Genius.RetrofitAboutGeniusData
import com.wotin.geniustest.RetrofitInterface.RetrofitServerCheck
import com.wotin.geniustest.RoomMethod.DeleteRoomMethod
import com.wotin.geniustest.RoomMethod.GetRoomMethod
import com.wotin.geniustest.RoomMethod.UserRoomMethod
import com.wotin.geniustest.Service.ConcentractionTestHeartManagementService
import com.wotin.geniustest.Service.QuicknessTestHeartManagementService
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity(),  NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serverCheck()

        // 3초마다 윈도우 조정해주는 메소드 실행.
        controlWindowOnTimer()

        navigation_button.setOnClickListener {
            layout_drawer.openDrawer(GravityCompat.START)
        }

        navigation_view.setNavigationItemSelectedListener(this)
        setNavigationHeaderLayout()

        fragment_view_pager.adapter =
            TabLayoutFragmentPagerAdapter(
                supportFragmentManager
            )

        tab_layout.setupWithViewPager(fragment_view_pager)
        for(i in 0 until tab_layout.tabCount) {
            when(i) {
                0 -> {
                    tab_layout.getTabAt(i)!!.setIcon(R.drawable.practice)
                    tab_layout.getTabAt(i)!!.text = "연습하기"
                }
                1 -> {
                    tab_layout.getTabAt(i)!!.setIcon(R.drawable.genius)
                    tab_layout.getTabAt(i)!!.text = "테스트"
                }
                2 -> {
                    tab_layout.getTabAt(i)!!.setIcon(R.drawable.ranking)
                    tab_layout.getTabAt(i)!!.text = "랭킹"
                }
            }
        }
        Objects.requireNonNull(tab_layout.getTabAt(0))!!.select()
    }

    private fun serverCheck() {
        var okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
        val baseUrl = "http://220.72.174.101:8080"
        var retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val geniusTestServerCheck = retrofit.create(RetrofitServerCheck::class.java)

        geniusTestServerCheck.serverCheck().enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "에러", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("TAG", "geniusTestServerCheck onResponse ${response.body()!!}")
                if(response.body()!!.get("from_time").asString.isNotEmpty()) {
                    val fromTime = response.body()!!.get("from_time").asString
                    val toTime = response.body()!!.get("to_time").asString
                    server_check_from_time_textview.text = fromTime
                    server_check_to_time_textview.text = toTime
                    window!!.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    server_check_layout.visibility = View.VISIBLE
                }
            }

        })
    }

    //3초마다 윈도우를 조정해주는 메소드.
    private fun controlWindowOnTimer() {
        timer(period = 3000)
        {
            runOnUiThread {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logout -> {
                DeleteRoomMethod().deleteUserDataAndGeniusTestAndPracticeData(applicationContext)
                deleteUserDataSharedPreference()
                DeleteRoomMethod().deleteTestModeData(applicationContext)
                stopService(Intent(this, ConcentractionTestHeartManagementService::class.java))
                stopService(Intent(this, QuicknessTestHeartManagementService::class.java))
                cancelAlarm()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            R.id.secession -> {
                val intent = Intent(this, DeleteUserActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        layout_drawer.closeDrawers()
        return true
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

    private fun setNavigationHeaderLayout() {
        val headerView = LayoutInflater.from(this).inflate(R.layout.navigation_view_header_layout, null)
        val userLevelImageView = headerView.findViewById<ImageView>(R.id.user_level_imageview_header_layout)
        val userNameTextView = headerView.findViewById<TextView>(R.id.user_name_textview_header_layout)
        val userIdTextView = headerView.findViewById<TextView>(R.id.user_id_textview_header_layout)
        val userLevelTextView = headerView.findViewById<TextView>(R.id.user_level_textview_header_layout)
        val userData = UserRoomMethod().getUserData(applicationContext)
        val userGeniusTestData = GetRoomMethod().getGeniusTestData(applicationContext)
        headerView.setOnClickListener {
            val intent = Intent(this, UserInformationActivity::class.java)
            intent.putExtra("userId", userData.id)
            startActivity(intent)
            finish()
        }
        userNameTextView.text = userData.name
        userIdTextView.text = EncryptionAndDetoxification().encryptionAndDetoxification(userData.id)
        userLevelTextView.text = userGeniusTestData.level
        when(userLevelTextView.text.toString()) {
            "초보" -> userLevelImageView.setImageResource(R.drawable.bad_brain)
            "중수" -> userLevelImageView.setImageResource(R.drawable.normal_brain)
            "고수" -> userLevelImageView.setImageResource(R.drawable.good_brain)
            "천재" -> userLevelImageView.setImageResource(R.drawable.genius)
        }
        navigation_view.addHeaderView(headerView)
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