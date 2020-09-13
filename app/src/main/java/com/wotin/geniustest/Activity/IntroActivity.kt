package com.wotin.geniustest.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.wotin.geniustest.Activity.LoginAndSignUp.LoginActivity
import com.wotin.geniustest.CustomClass.UserCustomClass
import com.wotin.geniustest.DB.UserDB
import com.wotin.geniustest.R
import com.wotin.geniustest.deleteUserDataAndGeniusTestAndPracticeData
import java.lang.Exception

class IntroActivity : AppCompatActivity() {
    var handler: Handler? = null
    var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

    }

    private fun getUserData() : UserCustomClass {
        val userDB : UserDB = Room.databaseBuilder(
            applicationContext,
            UserDB::class.java, "user.db"
        ).allowMainThreadQueries()
            .build()
        val userData : UserCustomClass = userDB.userDB().getAll()
        return userData
    }

    override fun onResume() {
        super.onResume()
        runnable = Runnable {
            try {
                val userData = getUserData()
                val intent = if(userData == null) {
                    Intent(this, LoginActivity::class.java)
                } else {
                    Intent(this, MainActivity::class.java)
                }
                startActivity(intent)
                finish()
            } catch (e : Exception) {
                deleteUserDataAndGeniusTestAndPracticeData(applicationContext)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        handler = Handler()
        handler?.run {
            postDelayed(runnable, 3000)
        }
    }

    override fun onPause() {
        super.onPause()

        handler?.removeCallbacks(runnable)
    }
}