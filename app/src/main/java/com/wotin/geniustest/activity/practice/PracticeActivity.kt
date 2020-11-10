package com.wotin.geniustest.activity.practice

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.wotin.geniustest.activity.MainActivity
import com.wotin.geniustest.adapter.practice.PracticeModeRecyclerViewAdapter
import com.wotin.geniustest.adapter.practice.PracticeQuicknessSlidingUpPanelRecyclerViewAdapter
import com.wotin.geniustest.customClass.geniusPractice.GeniusPracticeDataCustomClass
import com.wotin.geniustest.customClass.geniusPractice.PracticeModeCustomClass
import com.wotin.geniustest.R
import com.wotin.geniustest.roomMethod.GetRoomMethod
import kotlinx.android.synthetic.main.activity_practice.*
import kotlin.concurrent.timer

class PracticeActivity : AppCompatActivity(),
    PracticeModeRecyclerViewAdapter.QuicknessModeClickedInterface {

    lateinit var recyclerViewAdapter: PracticeModeRecyclerViewAdapter
    lateinit var modeList: ArrayList<PracticeModeCustomClass>

    lateinit var geniusPracticeData: GeniusPracticeDataCustomClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)

        setBottomDragView()

        close_practice_drag_layout_button.setOnClickListener {
            practice_sliding_up_panel_layout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
        }



        geniusPracticeData = GetRoomMethod().getGeniusPracticeData(applicationContext)


        modeList = arrayListOf(
            PracticeModeCustomClass(
                "기억력 테스트",
                geniusPracticeData.memoryScore.toInt(),
                geniusPracticeData.memoryDifference + "%"
            ),
            PracticeModeCustomClass(
                "집중력 테스트",
                geniusPracticeData.concentractionScore.toInt(),
                geniusPracticeData.concentractionDifference + "%"
            ),
            PracticeModeCustomClass(
                "순발력 테스트",
                geniusPracticeData.quicknessScore.toFloat().toInt(),
                geniusPracticeData.quicknessDifference + "%"
            )
        )

        // 3초마다 윈도우를 조정해주는 메소드 실행.
        controlWindowOnTimer()

        recyclerViewAdapter =
            PracticeModeRecyclerViewAdapter(
                modeList,
                this
            )
        practice_mode_recyclerview.apply {
            adapter = recyclerViewAdapter
            layoutManager =
                LinearLayoutManager(this@PracticeActivity, LinearLayoutManager.VERTICAL, false)
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

    private fun setBottomDragView() {
        val brainModeList = arrayListOf<String>("우뇌형", "좌뇌형")
        val dragRecyclerViewAdapter = PracticeQuicknessSlidingUpPanelRecyclerViewAdapter(brainModeList)
        practice_drag_recycler_view.apply {
            adapter = dragRecyclerViewAdapter
            layoutManager = LinearLayoutManager(this@PracticeActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    override fun quicknessModeClicked() {
        practice_sliding_up_panel_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
    }


}