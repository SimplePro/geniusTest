package com.wotin.geniustest.activity.practice

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.wotin.geniustest.adapter.practice.PracticeQuicknessRecyclerViewAdapter
import com.wotin.geniustest.R
import com.wotin.geniustest.databinding.ActivityPracticeQuicknessBinding
import com.wotin.geniustest.retrofitBuilder.GeniusRetrofitBuilder.geniusDataDifferenceApiService
import com.wotin.geniustest.roomMethod.GetRoomMethod
import com.wotin.geniustest.roomMethod.UpdateRoomMethod
import com.wotin.geniustest.networkState
import com.wotin.geniustest.viewModel.GeniusTestViewModel
import kotlinx.android.synthetic.main.activity_practice_quickness.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class PracticeQuicknessActivity : AppCompatActivity(), PracticeQuicknessRecyclerViewAdapter.ItemClickListener {

    var counter = 5000
    var quicknessList : ArrayList<String> = arrayListOf()
    var currentColor = ""
    lateinit var quicknessRecyclerViewAdapter : PracticeQuicknessRecyclerViewAdapter
    lateinit var t : Timer
    lateinit var tt : TimerTask

    var brain_mode = "right_brain"

    lateinit var mBinding : ActivityPracticeQuicknessBinding
    val geniusTestViewModel : GeniusTestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_practice_quickness)
        mBinding.viewModel = geniusTestViewModel
        mBinding.lifecycleOwner = this

        if(intent.hasExtra("brain_mode")) brain_mode = intent.getStringExtra("brain_mode")!!

        setQuicknessList()
        quicknessRecyclerViewAdapter = PracticeQuicknessRecyclerViewAdapter(quicknessList, this)

        mBinding.practiceQuicknessRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@PracticeQuicknessActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = quicknessRecyclerViewAdapter
            setHasFixedSize(true)
        }

        prog()

        go_to_mainactivity_from_practice_quickness_activity_imageview.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            startActivity(intent)
            finish()
        }

        practice_quickness_result_confirm_button.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun restart() {
        geniusTestViewModel.plusScore()
        setQuicknessList()
        t.cancel()
        tt.cancel()
        counter = if(counter >= 200) (counter * 0.95).toInt() else 200
        prog()
    }

    private fun prog() {
        t = Timer()
        mBinding.practiceQuicknessTimerProgressbar.max = counter
        tt = object : TimerTask() {
            override fun run() {
                counter -= 1
                mBinding.practiceQuicknessTimerProgressbar.progress = counter
                if(counter <= 0) {
                    t.cancel()
                    tt.cancel()
                    runOnUiThread {
                        mBinding.practiceQuicknessGameLayout.visibility = View.GONE
                        mBinding.practiceQuicknessResultLayout.visibility = View.VISIBLE
                        val connectivityManager : ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                        if(networkState(connectivityManager)) {
                            Log.d("TAG  ", "run: score is ${geniusTestViewModel.score.value}")
                            postDataToServer(geniusTestViewModel.score.value!!)
                        }
                    }
                }
            }
        }

        t.schedule(tt, 0, 10)
    }

    override fun onBackPressed() {
        val intent = Intent(this, PracticeActivity::class.java)
        startActivity(intent)
        t.cancel()
        tt.cancel()
        finish()
    }

    private fun postDataToServer(score : Int) {
        val geniusPracticeData = GetRoomMethod().getGeniusPracticeData(applicationContext)
        val uId = geniusPracticeData.UniqueId
        Log.d("TAG", "score is $score, uId is $uId")
        geniusDataDifferenceApiService.getGeniusPracticeQuicknessDifference(score.toString().toInt().toString(), uId).enqueue(object :
            Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "에러", Toast.LENGTH_LONG).show()
                Log.d("TAG", "postDataToServer PracticeDifference error is $t")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    Log.d("TAG", "postDataToServer PracticeDifference data is ${response.body()}")
                    val practiceQuicknessDifference = response.body()!!.get("practice_quickness_difference")
                    if(practiceQuicknessDifference.asString.isNotEmpty()) {
                        geniusPracticeData.quicknessScore = score.toString()
                        geniusPracticeData.quicknessDifference = practiceQuicknessDifference.asString
                        UpdateRoomMethod().updateGeniusPracticeData(context = applicationContext, geniusPracticeData = geniusPracticeData)
                    }
                } catch (e : Exception) {
                    Toast.makeText(applicationContext, "에러", Toast.LENGTH_LONG).show()
                    Log.d("TAG", "postDataToServer PracticeDifference error is ${e.message}")
                }
            }

        })
    }

    private fun setQuicknessList() {
        quicknessList.removeAll(quicknessList)
        currentColor = arrayListOf<String>("빨강", "주황", "노랑", "연두", "초록", "하늘", "파랑", "보라").random()
        when(currentColor) {
            "빨강" -> {
                mBinding.practiceQuicknessColorTextview.setTextColor(resources.getColor(R.color.colorRed))
            }
            "주황" -> {
                mBinding.practiceQuicknessColorTextview.setTextColor(resources.getColor(R.color.colorOrange))
            }
            "노랑" -> {
                mBinding.practiceQuicknessColorTextview.setTextColor(resources.getColor(R.color.colorYellow))
            }
            "연두" -> {
                mBinding.practiceQuicknessColorTextview.setTextColor(resources.getColor(R.color.colorLightGreen))
            }
            "초록" -> {
                mBinding.practiceQuicknessColorTextview.setTextColor(resources.getColor(R.color.colorGreen))
            }
            "하늘" -> {
                mBinding.practiceQuicknessColorTextview.setTextColor(resources.getColor(R.color.colorSkyBlue))
            }
            "파랑" -> {
                mBinding.practiceQuicknessColorTextview.setTextColor(resources.getColor(R.color.colorBlue))
            }
            "보라" -> {
                mBinding.practiceQuicknessColorTextview.setTextColor(resources.getColor(R.color.colorPurple))
            }
        }
        val fakeColorList = mutableListOf<String>("빨강", "주황", "노랑", "연두", "초록", "하늘", "파랑", "보라")
        fakeColorList.remove(currentColor)
        val fakeColor = fakeColorList.random()
        mBinding.practiceQuicknessColorTextview.text = fakeColor
        val randIndex = Random().nextInt(2) + 1
        Log.d("TAG", "setQuicknessList: currentColor is $currentColor randIndex is $randIndex")
        for(i in 0 .. 1) {
            if(i == randIndex - 1) {
                quicknessList.add(currentColor)
            } else {
                quicknessList.add(fakeColor)
            }
        }
        quicknessRecyclerViewAdapter = PracticeQuicknessRecyclerViewAdapter(quicknessList, this)
        mBinding.practiceQuicknessRecyclerview.apply {
            adapter = quicknessRecyclerViewAdapter
            setHasFixedSize(true)
        }
    }

    override fun itemClick(clickedColor: String) {
        if(clickedColor == currentColor && brain_mode == "right_brain") restart()
        else if (clickedColor != currentColor && brain_mode == "left_brain") restart()
        else {
            val vib = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vib.vibrate(500)
            counter -= 100
        }
    }

}