package com.wotin.geniustest.Activity.Practice

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import com.google.gson.JsonObject
import com.wotin.geniustest.R
import com.wotin.geniustest.RetrofitInterface.Genius.RetrofitAboutGeniusData
import com.wotin.geniustest.RoomMethod.GetRoomMethod
import com.wotin.geniustest.RoomMethod.UpdateRoomMethod
import com.wotin.geniustest.networkState
import kotlinx.android.synthetic.main.activity_practice_memory.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit

class PracticeMemoryActivity : AppCompatActivity() {

    var DURATION = 30000
    var COUNTER = 5000

    val handler : Handler = Handler()
    lateinit var runnable : Runnable

    lateinit var t : Timer
    lateinit var tt : TimerTask

    var score = 0
    var problemNum = ""
    var answerNum = ""

    lateinit var retrofit: Retrofit
    lateinit var geniusDataDifferenceApiService: RetrofitAboutGeniusData
    lateinit var okHttpClient: OkHttpClient
    val baseUrl = "http://220.72.174.101:8080"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_memory)

        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)

        runnable = Runnable {
            setButtonEnabledTrue()
            prog()
            practice_memory_answer_and_problem_textview.text = "???"
            practice_memory_skip_button.visibility = View.GONE
            practice_memory_timer_progressbar.visibility = View.VISIBLE
            practice_memory_answer_and_problem_textview.visibility = View.VISIBLE
            answerNum = ""
            practice_memory_answer_and_problem_textview.animation = (null)
        }

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

        start()

        go_to_mainactivity_from_practice_memory_activity_imageview.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            startActivity(intent)
            finish()
        }

        practice_memory_result_confirm_button.setOnClickListener {
            startActivity(Intent(this, PracticeActivity::class.java))
            finish()
        }

        practice_num_one_button.setOnClickListener {
            answerNum += "1"
            practice_memory_answer_and_problem_textview.text = answerNum
            if(answerNum == problemNum) start()
        }

        practice_num_two_button.setOnClickListener {
            answerNum += "2"
            practice_memory_answer_and_problem_textview.text = answerNum
            if(answerNum == problemNum) start()
        }
        practice_num_three_button.setOnClickListener {
            answerNum += "3"
            practice_memory_answer_and_problem_textview.text = answerNum
            if(answerNum == problemNum) start()
        }
        practice_num_four_button.setOnClickListener {
            answerNum += "4"
            practice_memory_answer_and_problem_textview.text = answerNum
            if(answerNum == problemNum) start()
        }
        practice_num_five_button.setOnClickListener {
            answerNum += "5"
            practice_memory_answer_and_problem_textview.text = answerNum
            if(answerNum == problemNum) start()
        }
        practice_num_six_button.setOnClickListener {
            answerNum += "6"
            practice_memory_answer_and_problem_textview.text = answerNum
            if(answerNum == problemNum) start()
        }
        practice_num_seven_button.setOnClickListener {
            answerNum += "7"
            practice_memory_answer_and_problem_textview.text = answerNum
            if(answerNum == problemNum) start()
        }
        practice_num_eight_button.setOnClickListener {
            answerNum += "8"
            practice_memory_answer_and_problem_textview.text = answerNum
            if(answerNum == problemNum) start()
        }
        practice_num_nine_button.setOnClickListener {
            answerNum += "9"
            practice_memory_answer_and_problem_textview.text = answerNum
            if(answerNum == problemNum) start()
        }
        practice_num_zero_button.setOnClickListener {
            answerNum += "0"
            practice_memory_answer_and_problem_textview.text = answerNum
            if(answerNum == problemNum) start()
        }

        practice_backspace_button.setOnClickListener {
            if (answerNum.isEmpty() || answerNum.length == 1) {
                answerNum = ""
                practice_memory_answer_and_problem_textview.text = "???"
            } else {
                answerNum = answerNum.substring(0, answerNum.length - 1)
                practice_memory_answer_and_problem_textview.text = answerNum
            }
        }

        practice_all_delete_button.setOnClickListener {
            answerNum = ""
            practice_memory_answer_and_problem_textview.text = "???"
        }

        practice_memory_skip_button.setOnClickListener {
            handler.removeCallbacks(runnable)
            setButtonEnabledTrue()
            prog()
            answerNum = ""
            practice_memory_skip_button.visibility = View.GONE
            practice_memory_timer_progressbar.visibility = View.VISIBLE
            practice_memory_answer_and_problem_textview.text = "???"
            practice_memory_answer_and_problem_textview.visibility = View.VISIBLE
            practice_memory_answer_and_problem_textview.animation = (null)
        }


    }

    private fun startAlphaAnimation() {
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.interpolator = DecelerateInterpolator()
        fadeOut.duration = DURATION.toLong()

        val animation = AnimationSet(false)
        animation.addAnimation(fadeOut)
        practice_memory_answer_and_problem_textview.animation = animation
    }

    private fun start() {
        score += 1
        problemNum = ""
        DURATION = if(DURATION >= 3000) (DURATION * 0.95).toInt() else 3000
        COUNTER = 10000
        setButtonEnabledFalse()
        practice_memory_skip_button.visibility = View.VISIBLE
        practice_memory_timer_progressbar.visibility = View.GONE
        when {
            score >= 20 -> {
                for(i in 0 .. 12) {
                    problemNum += Random().nextInt(9).toString()
                }
            }
            score >= 10 -> {
                for(i in 0 .. 7) {
                    problemNum += Random().nextInt(9).toString()
                }
            }
            else -> {
                for(i in 0 .. 4) {
                    problemNum += Random().nextInt(9).toString()
                }
            }
        }
        practice_memory_answer_and_problem_textview.text = problemNum
        practice_memory_score_textview.text = score.toString()
        practice_memory_result_textview.text = score.toString()
        startAlphaAnimation()
        try {
            t.cancel()
            tt.cancel()
        } catch (e : Exception) {
            Log.d("TAG", "PracticeMemoryActivity - start : error is $e")
        }
        handler.run {
            postDelayed(runnable, DURATION.toLong())
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, PracticeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun prog() {
        t = Timer()
        practice_memory_timer_progressbar.max = COUNTER
        tt = object : TimerTask() {
            override fun run() {
                COUNTER -= 1
                practice_memory_timer_progressbar.progress = COUNTER
                if((COUNTER / 100) == 0) {
                    t.cancel()
                    tt.cancel()
                    runOnUiThread {
                        practice_memory_game_layout.visibility = View.GONE
                        practice_memory_result_layout.visibility = View.VISIBLE
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

    private fun postDataToServer(score: Int) {
        val geniusPracticeData = GetRoomMethod().getGeniusPracticeData(applicationContext)
        val uId = geniusPracticeData.UniqueId
        Log.d("TAG", "score is $score, uId is $uId")
        geniusDataDifferenceApiService.getGeniusPracticeMemoryDifference(score.toString().toInt().toString(), uId).enqueue(object :
            Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "에러", Toast.LENGTH_LONG).show()
                Log.d("TAG", "postDataToServer PracticeDifference error is $t")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    Log.d("TAG", "postDataToServer PracticeDifference data is ${response.body()}")
                    val practiceMemoryDifference = response.body()!!.get("practice_memory_difference")
                    if(practiceMemoryDifference.asString.isNotEmpty()) {
                        geniusPracticeData.memoryScore = score.toString()
                        geniusPracticeData.memoryDifference = practiceMemoryDifference.asString
                        UpdateRoomMethod().updateGeniusPracticeData(context = applicationContext, geniusPracticeData = geniusPracticeData)
                    }
                } catch (e : Exception) {
                    Toast.makeText(applicationContext, "에러", Toast.LENGTH_LONG).show()
                    Log.d("TAG", "postDataToServer PracticeDifference error is ${e.message}")
                }
            }

        })
    }

    private fun setButtonEnabledFalse() {
        practice_num_one_button.isEnabled = false
        practice_num_two_button.isEnabled = false
        practice_num_three_button.isEnabled = false
        practice_num_four_button.isEnabled = false
        practice_num_five_button.isEnabled = false
        practice_num_six_button.isEnabled = false
        practice_num_seven_button.isEnabled = false
        practice_num_eight_button.isEnabled = false
        practice_num_nine_button.isEnabled = false
        practice_num_zero_button.isEnabled = false
        practice_backspace_button.isEnabled = false
        practice_all_delete_button.isEnabled = false
    }

    private fun setButtonEnabledTrue() {
        practice_num_one_button.isEnabled = true
        practice_num_two_button.isEnabled = true
        practice_num_three_button.isEnabled = true
        practice_num_four_button.isEnabled = true
        practice_num_five_button.isEnabled = true
        practice_num_six_button.isEnabled = true
        practice_num_seven_button.isEnabled = true
        practice_num_eight_button.isEnabled = true
        practice_num_nine_button.isEnabled = true
        practice_num_zero_button.isEnabled = true
        practice_backspace_button.isEnabled = true
        practice_all_delete_button.isEnabled = true
    }

}