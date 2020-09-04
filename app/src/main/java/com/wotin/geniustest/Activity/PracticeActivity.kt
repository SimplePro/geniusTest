package com.wotin.geniustest.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.wotin.geniustest.Adapter.PracticeModeRecyclerViewAdapter
import com.wotin.geniustest.CustomClass.GeniusPractice.GeniusPracticeDataCustomClass
import com.wotin.geniustest.CustomClass.ModeCustomClass
import com.wotin.geniustest.R
import com.wotin.geniustest.getGeniusPracticeData
import kotlinx.android.synthetic.main.activity_practice.*
import kotlin.concurrent.timer

class PracticeActivity : AppCompatActivity() {

    lateinit var recyclerViewAdapter : PracticeModeRecyclerViewAdapter
    lateinit var modeList : ArrayList<ModeCustomClass>

    lateinit var geniusPracticeData : GeniusPracticeDataCustomClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)

        geniusPracticeData = getGeniusPracticeData(applicationContext)

        modeList = arrayListOf(ModeCustomClass("기억력 테스트", geniusPracticeData.memoryScore.toInt(), geniusPracticeData.memoryDifference + "%"),
            ModeCustomClass("집중력 테스트", geniusPracticeData.concentractionScore.toInt(), geniusPracticeData.concentractionDifference + "%"))

        // 3초마다 윈도우를 조정해주는 메소드 실행.
        controlWindowOnTimer()

        recyclerViewAdapter = PracticeModeRecyclerViewAdapter(modeList)
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