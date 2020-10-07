package com.wotin.geniustest.Activity.Test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.wotin.geniustest.Activity.MainActivity
import com.wotin.geniustest.Adapter.Test.TestModeRecyclerViewAdapter
import com.wotin.geniustest.Adapter.Test.TestQuicknessSlidingUpPanelRecyclerViewAdapter
import com.wotin.geniustest.CustomClass.GeniusTest.GeniusTestDataCustomClass
import com.wotin.geniustest.CustomClass.GeniusTest.TestModeCustomClass
import com.wotin.geniustest.R
import com.wotin.geniustest.RoomMethod.GetRoomMethod
import com.wotin.geniustest.RoomMethod.UpdateRoomMethod
import com.wotin.geniustest.Service.ConcentractionTestHeartManagementService
import com.wotin.geniustest.Service.MemoryTestHeartManagementService
import com.wotin.geniustest.Service.QuicknessTestHeartManagementService
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.timer

class TestActivity : AppCompatActivity(), TestModeRecyclerViewAdapter.ModeClickedInterface, QuicknessTestHeartManagementService.QuicknessTestHeartManagementIsSaved,
    ConcentractionTestHeartManagementService.ConcentractionTestHeartManagementIsSaved,
    MemoryTestHeartManagementService.memoryTestHeartManagementIsSaved {

    lateinit var recyclerViewAdapter: TestModeRecyclerViewAdapter
    lateinit var modeList: ArrayList<TestModeCustomClass>

    lateinit var geniusTestData: GeniusTestDataCustomClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        setModeList()

        setBottomDragView()

        close_test_drag_layout_button.setOnClickListener {
            test_sliding_up_panel_layout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
        }

        // 3초마다 윈도우를 조정해주는 메소드 실행.
        controlWindowOnTimer()

        test_refresh_layout.setOnRefreshListener {
            setModeList()
            test_refresh_layout.isRefreshing = false
        }


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

//        timer(period = 300) {
//            setModeList()
//        }

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
        if (mode == "기억력 테스트") {
            ContextCompat.startForegroundService(this, Intent(applicationContext, MemoryTestHeartManagementService::class.java))
            recyclerViewAdapter.notifyDataSetChanged()
            val service = MemoryTestHeartManagementService()
            service.setMemoryInterface(this)
        }
        else if(mode == "집중력 테스트") {
            ContextCompat.startForegroundService(this, Intent(applicationContext, ConcentractionTestHeartManagementService::class.java))
            recyclerViewAdapter.notifyDataSetChanged()
            val service = ConcentractionTestHeartManagementService()
            service.setConcentractionInterface(this)
        } else if (mode == "순발력 테스트") {
            test_sliding_up_panel_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
            recyclerViewAdapter.notifyDataSetChanged()
        }
    }

    private fun setModeList() {
        GlobalScope.launch {
            withContext(Dispatchers.Default) {

                geniusTestData = GetRoomMethod().getGeniusTestData(applicationContext)

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

                val geniusTestModeData = GetRoomMethod().getTestModeData(applicationContext)
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

                UpdateRoomMethod().updateTestModeData(applicationContext, geniusTestModeData[0])
                UpdateRoomMethod().updateTestModeData(applicationContext, geniusTestModeData[1])
                UpdateRoomMethod().updateTestModeData(applicationContext, geniusTestModeData[2])
            }
        }

        modeList = GetRoomMethod().getTestModeData(applicationContext)
        Log.d("TAG", "testModeList is $modeList")
        recyclerViewAdapter = TestModeRecyclerViewAdapter(modeList, this@TestActivity)
        test_mode_recyclerview.adapter = recyclerViewAdapter

    }


    override fun onResume() {
        super.onResume()
        Thread.sleep(500)
        setModeList()
    }

    override fun memoryTestHeartManagement() {
        Log.d("TAG", "memoryTestHeartManagement: start memoryTestHeartManagement")
        modeList = GetRoomMethod().getTestModeData(this.applicationContext)
        Log.d("TAG", "testHeartManagement: modeList is $modeList")
        recyclerViewAdapter = TestModeRecyclerViewAdapter(modeList, this)
        test_mode_recyclerview.apply {
            adapter = recyclerViewAdapter
        }
        recyclerViewAdapter.notifyDataSetChanged()
    }

    override fun quicknessTestHeartManagement() {
        Log.d("TAG", "quicknessTestHeartManagement: start quicknessTestHeartManagement")
        modeList = GetRoomMethod().getTestModeData(this.applicationContext)
        Log.d("TAG", "testHeartManagement: modeList is $modeList")
        recyclerViewAdapter = TestModeRecyclerViewAdapter(modeList, this)
        test_mode_recyclerview.apply {
            adapter = recyclerViewAdapter
        }
        recyclerViewAdapter.notifyDataSetChanged()
    }

    override fun concentractionTestHeartManagement() {
        Log.d("TAG", "concentractionTestHeartManagement: start concentractionTestHeartManagement")
        modeList = GetRoomMethod().getTestModeData(this.applicationContext)
        Log.d("TAG", "testHeartManagement: modeList is $modeList")
        recyclerViewAdapter = TestModeRecyclerViewAdapter(modeList, this)
        test_mode_recyclerview.apply {
            adapter = recyclerViewAdapter
        }
        recyclerViewAdapter.notifyDataSetChanged()
    }

    private fun setBottomDragView() {
        val brainModeList = arrayListOf<String>("우뇌형", "좌뇌형")
        val dragRecyclerViewAdapter = TestQuicknessSlidingUpPanelRecyclerViewAdapter(brainModeList)
        test_drag_recycler_view.apply {
            adapter = dragRecyclerViewAdapter
            layoutManager = LinearLayoutManager(this@TestActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

}