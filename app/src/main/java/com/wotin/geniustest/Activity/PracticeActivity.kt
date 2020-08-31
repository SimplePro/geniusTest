package com.wotin.geniustest.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.wotin.geniustest.Adapter.ModeRecyclerViewAdapter
import com.wotin.geniustest.CustomClass.ModeCustomClass
import com.wotin.geniustest.R
import kotlinx.android.synthetic.main.activity_practice.*
import kotlin.concurrent.timer

class PracticeActivity : AppCompatActivity() {

    lateinit var recyclerViewAdapter : ModeRecyclerViewAdapter
    val modeList : ArrayList<ModeCustomClass> = arrayListOf(ModeCustomClass("기억력 테스트", 1, "5%"), ModeCustomClass("집중력 테스트", 1, "10%"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)

        // 3초마다 윈도우를 조정해주는 메소드 실행.
        controlWindowOnTimer()

        recyclerViewAdapter = ModeRecyclerViewAdapter(modeList)
        practice_mode_recyclerview.apply {
            adapter = recyclerViewAdapter
            layoutManager = LinearLayoutManager(this@PracticeActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        practice_back_btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
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

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}