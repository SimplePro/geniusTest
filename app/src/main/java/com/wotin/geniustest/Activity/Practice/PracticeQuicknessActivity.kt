package com.wotin.geniustest.Activity.Practice

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonObject
import com.wotin.geniustest.Activity.MainActivity
import com.wotin.geniustest.Adapter.Practice.PracticeConcentractionRecyclerViewAdapter
import com.wotin.geniustest.R
import com.wotin.geniustest.RetrofitInterface.RetrofitAboutGeniusData
import com.wotin.geniustest.getGeniusPracticeData
import com.wotin.geniustest.networkState
import com.wotin.geniustest.updateGeniusPracticeData
import kotlinx.android.synthetic.main.activity_practice_quickness.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class PracticeQuicknessActivity : AppCompatActivity() {

    var score = 1
    var counter = 5000
    var itemCount = 49
    var quicknessList : ArrayList<String> = arrayListOf()
    lateinit var quicknessRecyclerViewAdapter : PracticeConcentractionRecyclerViewAdapter
    lateinit var t : Timer
    lateinit var tt : TimerTask

    lateinit var retrofit: Retrofit
    lateinit var geniusDataDifferenceApiService: RetrofitAboutGeniusData
    lateinit var okHttpClient: OkHttpClient
    val baseUrl = "http://220.72.174.101:8080"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_quickness)

        go_to_mainactivity_from_practice_quickness_activity_imageview.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun restart() {
        score += 1
        practice_quickness_score_textview.text = score.toString()
        practice_quickness_result_textview.text = score.toString()
        if(score % 5 == 0) {
            resetRecyclerViewGridLayout()
        }
        setConcentractionList()
        t.cancel()
        tt.cancel()
        counter = (5000 * 0.95).toInt()
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
                            postDataToServer(score = score)
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

    private fun setConcentractionList() {
        quicknessList.removeAll(quicknessList)
        when (Random().nextInt(5)) {
            0 -> {
                for(i in 0 .. itemCount) {
                    quicknessList.add("n")
                }
                val random2 = Random().nextInt(itemCount)
                quicknessList[random2] = "h"
            }
            1 -> {
                for(i in 0 .. itemCount) {
                    quicknessList.add("i")
                }
                val random2 = Random().nextInt(itemCount)
                quicknessList[random2] = "j"
            }
            2 -> {
                for(i in 0 .. itemCount) {
                    quicknessList.add("V")
                }
                val random2 = Random().nextInt(itemCount)
                quicknessList[random2] = "Y"
            }
            3 -> {
                for(i in 0 .. itemCount) {
                    quicknessList.add("q")
                }
                val random2 = Random().nextInt(itemCount)
                quicknessList[random2] = "p"
            }
            else -> {
                for(i in 0 .. itemCount) {
                    quicknessList.add("G")
                }
                val random2 = Random().nextInt(itemCount)
                quicknessList[random2] = "C"
            }
        }
        practice_quickness_recyclerview.apply {
            adapter = quicknessRecyclerViewAdapter
            setHasFixedSize(true)
        }
    }

    private fun resetRecyclerViewGridLayout() {
        when {
            score >= 20 -> {
                itemCount = 74
                val recyclerViewLayoutManager = GridLayoutManager(applicationContext, 15)
                practice_quickness_recyclerview.layoutManager = recyclerViewLayoutManager
            }
            score >= 15 -> {
                itemCount = 64
                val recyclerViewLayoutManager = GridLayoutManager(applicationContext, 13)
                practice_quickness_recyclerview.layoutManager = recyclerViewLayoutManager
            }
            score >= 10 -> {
                itemCount = 59
                val recyclerViewLayoutManager = GridLayoutManager(applicationContext, 12)
                practice_quickness_recyclerview.layoutManager = recyclerViewLayoutManager
            } else -> {
            itemCount = 49
        }
        }
        quicknessRecyclerViewAdapter.notifyDataSetChanged()
        practice_quickness_recyclerview.apply {
            adapter = quicknessRecyclerViewAdapter
            setHasFixedSize(true)
        }
    }

    private fun postDataToServer(score : Int) {
        val geniusPracticeData = getGeniusPracticeData(applicationContext)
        val uId = geniusPracticeData.UniqueId
        Log.d("TAG", "score is $score, uId is $uId")
        geniusDataDifferenceApiService.getGeniusPracticeDifference(score.toString(), uId).enqueue(object :
            Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "에러", Toast.LENGTH_LONG).show()
                Log.d("TAG", "postDataToServer PracticeDifference error is $t")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    Log.d("TAG", "postDataToServer PracticeDifference data is ${response.body()}")
                    val practiceConcentractionDifference = response.body()!!.get("practice_quickness_difference")
                    if(practiceConcentractionDifference.asString.isNotEmpty()) {
                        geniusPracticeData.quicknessScore = score.toString()
                        geniusPracticeData.quicknessDifference = practiceConcentractionDifference.asString
                        updateGeniusPracticeData(context = applicationContext, geniusPracticeData = geniusPracticeData)
                    }
                } catch (e : Exception) {
                    Toast.makeText(applicationContext, "에러", Toast.LENGTH_LONG).show()
                    Log.d("TAG", "postDataToServer PracticeDifference error is ${e.message}")
                }
            }

        })
    }

}