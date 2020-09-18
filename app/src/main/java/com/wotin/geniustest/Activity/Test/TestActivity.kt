package com.wotin.geniustest.Activity.Test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.wotin.geniustest.*
import com.wotin.geniustest.Activity.MainActivity
import com.wotin.geniustest.Adapter.Practice.PracticeModeRecyclerViewAdapter
import com.wotin.geniustest.Adapter.Test.TestModeRecyclerViewAdapter
import com.wotin.geniustest.CustomClass.GeniusPractice.GeniusPracticeDataCustomClass
import com.wotin.geniustest.CustomClass.GeniusTest.GeniusTestDataCustomClass
import com.wotin.geniustest.CustomClass.PracticeModeCustomClass
import com.wotin.geniustest.CustomClass.TestModeCustomClass
import kotlinx.android.synthetic.main.activity_practice.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlin.concurrent.timer

class TestActivity : AppCompatActivity() {

    lateinit var recyclerViewAdapter: TestModeRecyclerViewAdapter
    lateinit var modeList: ArrayList<TestModeCustomClass>

    lateinit var geniusPracticeData: GeniusTestDataCustomClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        geniusPracticeData = getGeniusTestData(applicationContext)

        modeList = arrayListOf(
            TestModeCustomClass(
                "기억력 테스트",
                geniusPracticeData.memoryScore.toInt(),
                geniusPracticeData.memoryDifference + "%"
            ),
            TestModeCustomClass(
                "집중력 테스트",
                geniusPracticeData.concentractionScore.toInt(),
                geniusPracticeData.concentractionDifference + "%"
            ),
            TestModeCustomClass(
                "순발력 테스트",
                geniusPracticeData.quicknessScore.toFloat().toInt(),
                geniusPracticeData.quicknessDifference + "%"
            )
        )

        updateTestModeData(applicationContext, TestModeCustomClass("기억력 테스트", geniusPracticeData.memoryScore.toInt(),geniusPracticeData.memoryDifference + "%"))
        updateTestModeData(applicationContext, TestModeCustomClass("집중력 테스트", geniusPracticeData.concentractionScore.toInt(),geniusPracticeData.concentractionDifference + "%"))
        updateTestModeData(applicationContext, TestModeCustomClass("순발력 테스트", geniusPracticeData.quicknessScore.toFloat().toInt(),geniusPracticeData.quicknessDifference + "%"))

        modeList = getTestModeData(applicationContext)

        // 3초마다 윈도우를 조정해주는 메소드 실행.
        controlWindowOnTimer()


        // 3초마다 윈도우를 조정해주는 메소드 실행.
        controlWindowOnTimer()

        recyclerViewAdapter =
            TestModeRecyclerViewAdapter(
                modeList
            )
        test_mode_recyclerview.apply {
            adapter = recyclerViewAdapter
            layoutManager =
                LinearLayoutManager(this@TestActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        test_back_btn.setOnClickListener {
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