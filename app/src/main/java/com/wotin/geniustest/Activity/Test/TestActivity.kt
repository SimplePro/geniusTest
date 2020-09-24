package com.wotin.geniustest.Activity.Test

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.wotin.geniustest.*
import com.wotin.geniustest.Activity.MainActivity
import com.wotin.geniustest.Adapter.Test.TestModeRecyclerViewAdapter
import com.wotin.geniustest.CustomClass.GeniusTest.GeniusTestDataCustomClass
import com.wotin.geniustest.CustomClass.TestModeCustomClass
import com.wotin.geniustest.Service.ConcentractionTestHeartManagementService
import com.wotin.geniustest.Service.QuicknessTestHeartManagementService
import kotlinx.android.synthetic.main.activity_test.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer

class TestActivity : AppCompatActivity(), TestModeRecyclerViewAdapter.ModeClickedInterface, QuicknessTestHeartManagementService.QuicknessTestHeartManagementIsSaved,
    ConcentractionTestHeartManagementService.ConcentractionTestHeartManagementIsSaved {

    lateinit var recyclerViewAdapter: TestModeRecyclerViewAdapter
    lateinit var modeList: ArrayList<TestModeCustomClass>

    lateinit var geniusTestData: GeniusTestDataCustomClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        setModeList()

        // 3초마다 윈도우를 조정해주는 메소드 실행.
        controlWindowOnTimer()


        // 3초마다 윈도우를 조정해주는 메소드 실행.
        controlWindowOnTimer()

        recyclerViewAdapter =
            TestModeRecyclerViewAdapter(
                modeList,
                this
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

    override fun modeClicked(mode: String) {
        if(mode == "집중력 테스트") {
            ContextCompat.startForegroundService(this, Intent(applicationContext, ConcentractionTestHeartManagementService::class.java))
            recyclerViewAdapter.notifyDataSetChanged()
            val service = ConcentractionTestHeartManagementService()
            service.setConcentractionInterface(this)
        } else if (mode == "순발력 테스트") {
            ContextCompat.startForegroundService(this, Intent(applicationContext, QuicknessTestHeartManagementService::class.java))
            recyclerViewAdapter.notifyDataSetChanged()
            val service = QuicknessTestHeartManagementService()
            service.setQuicknessInterface(this)
        }
    }

    private fun setModeList() {
        geniusTestData = getGeniusTestData(applicationContext)

        modeList = arrayListOf(
            TestModeCustomClass(
                "기억력 테스트",
                geniusTestData.memoryScore.toInt(),
                geniusTestData.memoryDifference + "%"
            ),
            TestModeCustomClass(
                "집중력 테스트",
                geniusTestData.concentractionScore.toInt(),
                geniusTestData.concentractionDifference + "%"
            ),
            TestModeCustomClass(
                "순발력 테스트",
                geniusTestData.quicknessScore.toFloat().toInt(),
                geniusTestData.quicknessDifference + "%"
            )
        )

        val geniusTestModeData = getTestModeData(applicationContext)
        geniusTestModeData[0].apply {
            score = geniusTestData.memoryScore.toInt()
            difference = geniusTestData.memoryDifference
        }
        geniusTestModeData[1].apply {
            score = geniusTestData.concentractionScore.toInt()
            difference = geniusTestData.concentractionDifference
        }
        geniusTestModeData[2].apply {
            score = geniusTestData.quicknessScore.toFloat().toInt()
            difference = geniusTestData.quicknessDifference
        }

        updateTestModeData(applicationContext, geniusTestModeData[0])
        updateTestModeData(applicationContext, geniusTestModeData[1])
        updateTestModeData(applicationContext, geniusTestModeData[2])

        modeList = getTestModeData(applicationContext)
        Log.d("TAG", "testModeList is $modeList")
        recyclerViewAdapter = TestModeRecyclerViewAdapter(modeList, this)
        test_mode_recyclerview.adapter = recyclerViewAdapter
    }

    override fun onResume() {
        super.onResume()
        Thread.sleep(500)
        setModeList()
    }

    override fun quicknessTestHeartManagement() {
        Log.d("TAG", "quicknessTestHeartManagement: start quicknessTestHeartManagement")
        Handler().postDelayed({
            modeList = getTestModeData(applicationContext)
            Log.d("TAG", "testHeartManagement: modeList is $modeList")
            recyclerViewAdapter = TestModeRecyclerViewAdapter(modeList, this)
            test_mode_recyclerview.apply {
                adapter = recyclerViewAdapter
            }
            recyclerViewAdapter.notifyDataSetChanged()
        }, 500)
    }

    override fun concentractionTestHeartManagement() {
        Log.d("TAG", "concentractionTestHeartManagement: start concentractionTestHeartManagement")
        Handler().postDelayed({
            modeList = getTestModeData(applicationContext)
            Log.d("TAG", "testHeartManagement: modeList is $modeList")
            recyclerViewAdapter = TestModeRecyclerViewAdapter(modeList, this)
            test_mode_recyclerview.apply {
                adapter = recyclerViewAdapter
            }
            recyclerViewAdapter.notifyDataSetChanged()
        }, 500)
    }

}