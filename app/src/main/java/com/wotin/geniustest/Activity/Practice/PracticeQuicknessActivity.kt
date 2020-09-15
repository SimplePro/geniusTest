package com.wotin.geniustest.Activity.Practice

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.wotin.geniustest.Activity.MainActivity
import com.wotin.geniustest.Adapter.Practice.PracticeConcentractionRecyclerViewAdapter
import com.wotin.geniustest.Adapter.Practice.PracticeQuicknessRecyclerViewAdapter
import com.wotin.geniustest.R
import com.wotin.geniustest.RetrofitInterface.RetrofitAboutGeniusData
import com.wotin.geniustest.getGeniusPracticeData
import com.wotin.geniustest.networkState
import com.wotin.geniustest.updateGeniusPracticeData
import kotlinx.android.synthetic.main.activity_practice_concentraction.*
import kotlinx.android.synthetic.main.activity_practice_quickness.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class PracticeQuicknessActivity : AppCompatActivity(), PracticeQuicknessRecyclerViewAdapter.ItemClickListener {

    var score = 1
    var counter = 5000
    var quicknessList : ArrayList<String> = arrayListOf()
    var currentColor = ""
    lateinit var quicknessRecyclerViewAdapter : PracticeQuicknessRecyclerViewAdapter
    lateinit var t : Timer
    lateinit var tt : TimerTask

    lateinit var retrofit: Retrofit
    lateinit var geniusDataDifferenceApiService: RetrofitAboutGeniusData
    lateinit var okHttpClient: OkHttpClient
    val baseUrl = "http://220.72.174.101:8080"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_quickness)

        okHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        geniusDataDifferenceApiService = retrofit.create(RetrofitAboutGeniusData::class.java)

        setQuicknessList()
        quicknessRecyclerViewAdapter = PracticeQuicknessRecyclerViewAdapter(quicknessList, this)

        practice_quickness_recyclerview.apply {
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
        score += 1
        practice_quickness_score_textview.text = score.toString()
        practice_quickness_result_textview.text = score.toString()
        setQuicknessList()
        t.cancel()
        tt.cancel()
        counter = (counter * 0.95).toInt()
        prog()
    }

    private fun prog() {
        t = Timer()
        practice_quickness_timer_progressbar.max = counter
        tt = object : TimerTask() {
            override fun run() {
                counter -= 1
                practice_quickness_timer_progressbar.progress = counter
                if((counter / 100) == 0) {
                    t.cancel()
                    tt.cancel()
                    runOnUiThread {
                        practice_quickness_game_layout.visibility = View.GONE
                        practice_quickness_result_layout.visibility = View.VISIBLE
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
        val intent = Intent(this, PracticeActivity::class.java)
        startActivity(intent)
        t.cancel()
        tt.cancel()
        finish()
    }

    private fun postDataToServer(score : Int) {
        val geniusPracticeData = getGeniusPracticeData(applicationContext)
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
                        updateGeniusPracticeData(context = applicationContext, geniusPracticeData = geniusPracticeData)
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
                practice_quickness_color_textview.setTextColor(resources.getColor(R.color.colorRed))
            }
            "주황" -> {
                practice_quickness_color_textview.setTextColor(resources.getColor(R.color.colorOrange))
            }
            "노랑" -> {
                practice_quickness_color_textview.setTextColor(resources.getColor(R.color.colorYellow))
            }
            "연두" -> {
                practice_quickness_color_textview.setTextColor(resources.getColor(R.color.colorLightGreen))
            }
            "초록" -> {
                practice_quickness_color_textview.setTextColor(resources.getColor(R.color.colorGreen))
            }
            "하늘" -> {
                practice_quickness_color_textview.setTextColor(resources.getColor(R.color.colorSkyBlue))
            }
            "파랑" -> {
                practice_quickness_color_textview.setTextColor(resources.getColor(R.color.colorBlue))
            }
            "보라" -> {
                practice_quickness_color_textview.setTextColor(resources.getColor(R.color.colorPurple))
            }
        }
        val fakeColorList = mutableListOf<String>("빨강", "주황", "노랑", "연두", "초록", "하늘", "파랑", "보라")
        fakeColorList.remove(currentColor)
        val fakeColor = fakeColorList.random()
        practice_quickness_color_textview.text = fakeColor
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
        practice_quickness_recyclerview.apply {
            adapter = quicknessRecyclerViewAdapter
            setHasFixedSize(true)
        }
    }

    override fun itemClick(clickedColor: String) {
        if(clickedColor == currentColor) {
            restart()
        } else {
            val vib = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vib.vibrate(500)
            counter -= 100
        }
    }

}