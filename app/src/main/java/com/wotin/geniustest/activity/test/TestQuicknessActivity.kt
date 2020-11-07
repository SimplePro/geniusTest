package com.wotin.geniustest.activity.test

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.wotin.geniustest.*
import com.wotin.geniustest.adapter.Test.TestQuicknessRecyclerViewAdapter
import com.wotin.geniustest.retrofitBuilder.GeniusRetrofitBuilder.geniusDataDifferenceApiService
import com.wotin.geniustest.roomMethod.GetRoomMethod
import com.wotin.geniustest.roomMethod.UpdateRoomMethod
import kotlinx.android.synthetic.main.activity_test_quickness.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class TestQuicknessActivity : AppCompatActivity(), TestQuicknessRecyclerViewAdapter.ItemClickListener {

    var score = 1
    var counter = 5000
    var quicknessList : ArrayList<String> = arrayListOf()
    var currentColor = ""
    lateinit var quicknessRecyclerViewAdapter : TestQuicknessRecyclerViewAdapter
    lateinit var t : Timer
    lateinit var tt : TimerTask

    var brain_mode = "right_brain"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_quickness)

        if(intent.hasExtra("brain_mode")) {
            brain_mode = intent.getStringExtra("brain_mode")
        }

        setQuicknessList()
        quicknessRecyclerViewAdapter = TestQuicknessRecyclerViewAdapter(quicknessList, this)

        test_quickness_recyclerview.apply {
            layoutManager = LinearLayoutManager(this@TestQuicknessActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = quicknessRecyclerViewAdapter
            setHasFixedSize(true)
        }

        prog()

        go_to_mainactivity_from_test_quickness_activity_imageview.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
            finish()
        }

        test_quickness_result_confirm_button.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
            finish()
        }

        
    }


    private fun restart() {
        score += 1
        test_quickness_score_textview.text = score.toString()
        test_quickness_result_textview.text = score.toString()
        setQuicknessList()
        t.cancel()
        tt.cancel()
        counter = if(counter >= 200) (counter * 0.95).toInt() else 200
        prog()
    }

    private fun prog() {
        t = Timer()
        test_quickness_timer_progressbar.max = counter
        tt = object : TimerTask() {
            override fun run() {
                counter -= 1
                test_quickness_timer_progressbar.progress = counter
                if((counter / 100) == 0) {
                    t.cancel()
                    tt.cancel()
                    runOnUiThread {
                        test_quickness_game_layout.visibility = View.GONE
                        test_quickness_result_layout.visibility = View.VISIBLE
                        val connectivityManager : ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                        if(networkState(connectivityManager)) {
                            Log.d("TAG  ", "run: score is $score")
                            postDataToServer(score)
                        }
                    }
                }
            }
        }

        t.schedule(tt, 0, 10)
    }

    override fun onBackPressed() {
        val intent = Intent(this, TestActivity::class.java)
        startActivity(intent)
        t.cancel()
        tt.cancel()
        finish()
    }

    private fun postDataToServer(score : Int) {
        val geniusTestData = GetRoomMethod().getGeniusTestData(applicationContext)
        val uId = geniusTestData.UniqueId
        Log.d("TAG", "score is $score, uId is $uId")
        geniusDataDifferenceApiService.getGeniusTestQuicknessDifference(score.toString().toInt().toString(), uId).enqueue(object :
            Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "에러", Toast.LENGTH_LONG).show()
                Log.d("TAG", "postDataToServer TestDifference error is $t")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    Log.d("TAG", "postDataToServer TestDifference data is ${response.body()}")
                    val TestQuicknessDifference = response.body()!!.get("test_quickness_difference")
                    if(TestQuicknessDifference.asString.isNotEmpty()) {
                        geniusTestData.quicknessScore = score.toString()
                        geniusTestData.quicknessDifference = TestQuicknessDifference.asString
                        UpdateRoomMethod().updateGeniusTestData(context = applicationContext, geniusTestData = geniusTestData)
                    }
                } catch (e : Exception) {
                    Toast.makeText(applicationContext, "에러", Toast.LENGTH_LONG).show()
                    Log.d("TAG", "postDataToServer TestDifference error is ${e.message}")
                }
            }

        })
    }

    private fun setQuicknessList() {
        quicknessList.removeAll(quicknessList)
        currentColor = arrayListOf<String>("빨강", "주황", "노랑", "연두", "초록", "하늘", "파랑", "보라").random()
        when(currentColor) {
            "빨강" -> {
                test_quickness_color_textview.setTextColor(resources.getColor(R.color.colorRed))
            }
            "주황" -> {
                test_quickness_color_textview.setTextColor(resources.getColor(R.color.colorOrange))
            }
            "노랑" -> {
                test_quickness_color_textview.setTextColor(resources.getColor(R.color.colorYellow))
            }
            "연두" -> {
                test_quickness_color_textview.setTextColor(resources.getColor(R.color.colorLightGreen))
            }
            "초록" -> {
                test_quickness_color_textview.setTextColor(resources.getColor(R.color.colorGreen))
            }
            "하늘" -> {
                test_quickness_color_textview.setTextColor(resources.getColor(R.color.colorSkyBlue))
            }
            "파랑" -> {
                test_quickness_color_textview.setTextColor(resources.getColor(R.color.colorBlue))
            }
            "보라" -> {
                test_quickness_color_textview.setTextColor(resources.getColor(R.color.colorPurple))
            }
        }
        val fakeColorList = mutableListOf<String>("빨강", "주황", "노랑", "연두", "초록", "하늘", "파랑", "보라")
        fakeColorList.remove(currentColor)
        val fakeColor = fakeColorList.random()
        test_quickness_color_textview.text = fakeColor
        val randIndex = Random().nextInt(2) + 1
        Log.d("TAG", "setQuicknessList: currentColor is $currentColor randIndex is $randIndex")
        for(i in 0 .. 1) {
            if(i == randIndex - 1) {
                quicknessList.add(currentColor)
            } else {
                quicknessList.add(fakeColor)
            }
        }
        quicknessRecyclerViewAdapter = TestQuicknessRecyclerViewAdapter(quicknessList, this)
        test_quickness_recyclerview.apply {
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