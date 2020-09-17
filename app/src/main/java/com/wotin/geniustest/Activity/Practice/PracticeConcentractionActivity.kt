package com.wotin.geniustest.Activity.Practice

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonObject
import com.wotin.geniustest.Adapter.Practice.PracticeConcentractionRecyclerViewAdapter
import com.wotin.geniustest.R
import com.wotin.geniustest.RetrofitInterface.Genius.RetrofitAboutGeniusData
import com.wotin.geniustest.getGeniusPracticeData
import com.wotin.geniustest.networkState
import com.wotin.geniustest.updateGeniusPracticeData
import kotlinx.android.synthetic.main.activity_practice_concentraction.*
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

class PracticeConcentractionActivity : AppCompatActivity(), PracticeConcentractionRecyclerViewAdapter.ItemClickListener {

    var score = 1
    var counter = 5000
    var itemCount = 49
    var concentractionList : ArrayList<String> = arrayListOf()
    lateinit var concentractionRecyclerViewAdapter : PracticeConcentractionRecyclerViewAdapter
    lateinit var t : Timer
    lateinit var tt : TimerTask

    lateinit var retrofit: Retrofit
    lateinit var geniusDataDifferenceApiService: RetrofitAboutGeniusData
    lateinit var okHttpClient: OkHttpClient
    val baseUrl = "http://220.72.174.101:8080"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_concentraction)

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

        practice_concentraction_timer_progressbar.min = 0

        setConcentractionList()

        val recyclerViewLayoutManager = GridLayoutManager(applicationContext, 10)
        practice_concentraction_recyclerview.apply {
            layoutManager = recyclerViewLayoutManager
            adapter = concentractionRecyclerViewAdapter
            setHasFixedSize(true)
        }

        prog()

        go_to_mainactivity_from_practice_concentraction_activity_imageview.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            startActivity(intent)
            finish()
        }

        practice_concentraction_result_confirm_button.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun restart() {
        score += 1
        practice_concentraction_score_textview.text = score.toString()
        practice_concentraction_result_textview.text = score.toString()
        if(score % 5 == 0) {
            resetRecyclerViewGridLayout()
        }
        setConcentractionList()
        t.cancel()
        tt.cancel()
        counter = (counter * 0.95).toInt()
        prog()
    }

    private fun prog() {
        t = Timer()
        practice_concentraction_timer_progressbar.max = counter
        tt = object : TimerTask() {
            override fun run() {
                counter -= 1
                practice_concentraction_timer_progressbar.progress = counter
                if((counter / 100) == 0) {
                    t.cancel()
                    tt.cancel()
                    runOnUiThread {
                        practice_concentraction_game_layout.visibility = View.GONE
                        practice_concentraction_result_layout.visibility = View.VISIBLE
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

    override fun itemClicked(position: Int) {
        if(concentractionList[position] == "h" || concentractionList[position] == "j" || concentractionList[position] == "Y"
            || concentractionList[position] == "p" || concentractionList[position] == "C") {
            restart()
        }
        else {
            val vib = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vib.vibrate(500)
            counter -= 100
        }
    }

    private fun setConcentractionList() {
        concentractionList.removeAll(concentractionList)
        when (Random().nextInt(5)) {
            0 -> {
                for(i in 0 .. itemCount) {
                    concentractionList.add("n")
                }
                val random2 = Random().nextInt(itemCount)
                concentractionList[random2] = "h"
            }
            1 -> {
                for(i in 0 .. itemCount) {
                    concentractionList.add("i")
                }
                val random2 = Random().nextInt(itemCount)
                concentractionList[random2] = "j"
            }
            2 -> {
                for(i in 0 .. itemCount) {
                    concentractionList.add("V")
                }
                val random2 = Random().nextInt(itemCount)
                concentractionList[random2] = "Y"
            }
            3 -> {
                for(i in 0 .. itemCount) {
                    concentractionList.add("q")
                }
                val random2 = Random().nextInt(itemCount)
                concentractionList[random2] = "p"
            }
            else -> {
                for(i in 0 .. itemCount) {
                    concentractionList.add("G")
                }
                val random2 = Random().nextInt(itemCount)
                concentractionList[random2] = "C"
            }
        }
        concentractionRecyclerViewAdapter =
            PracticeConcentractionRecyclerViewAdapter(
                concentractionList,
                this
            )
        practice_concentraction_recyclerview.apply {
            adapter = concentractionRecyclerViewAdapter
            setHasFixedSize(true)
        }
    }

    private fun resetRecyclerViewGridLayout() {
        when {
            score >= 20 -> {
                itemCount = 74
                val recyclerViewLayoutManager = GridLayoutManager(applicationContext, 15)
                practice_concentraction_recyclerview.layoutManager = recyclerViewLayoutManager
            }
            score >= 15 -> {
                itemCount = 64
                val recyclerViewLayoutManager = GridLayoutManager(applicationContext, 13)
                practice_concentraction_recyclerview.layoutManager = recyclerViewLayoutManager
            }
            score >= 10 -> {
                itemCount = 59
                val recyclerViewLayoutManager = GridLayoutManager(applicationContext, 12)
                practice_concentraction_recyclerview.layoutManager = recyclerViewLayoutManager
            } else -> {
            itemCount = 49
        }
        }
        concentractionRecyclerViewAdapter.notifyDataSetChanged()
        practice_concentraction_recyclerview.apply {
            adapter = concentractionRecyclerViewAdapter
            setHasFixedSize(true)
        }
    }

    private fun postDataToServer(score : Int) {
        val geniusPracticeData = getGeniusPracticeData(applicationContext)
        val uId = geniusPracticeData.UniqueId
        Log.d("TAG", "score is $score, uId is $uId")
        geniusDataDifferenceApiService.getGeniusPracticeConcentractionDifference(score.toString(), uId).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "에러", Toast.LENGTH_LONG).show()
                Log.d("TAG", "postDataToServer PracticeDifference error is $t")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    Log.d("TAG", "postDataToServer PracticeDifference data is ${response.body()}")
                    val practiceConcentractionDifference = response.body()!!.get("practice_concentraction_difference")
                    if(practiceConcentractionDifference.asString.isNotEmpty()) {
                        geniusPracticeData.concentractionScore = score.toString()
                        geniusPracticeData.concentractionDifference = practiceConcentractionDifference.asString
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