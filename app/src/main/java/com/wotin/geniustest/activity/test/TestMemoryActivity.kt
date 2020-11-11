package com.wotin.geniustest.activity.test

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
import com.wotin.geniustest.retrofitBuilder.GeniusRetrofitBuilder.geniusDataDifferenceApiService
import com.wotin.geniustest.roomMethod.GetRoomMethod
import com.wotin.geniustest.roomMethod.UpdateRoomMethod
import com.wotin.geniustest.networkState
import kotlinx.android.synthetic.main.activity_test_memory.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*

class TestMemoryActivity : AppCompatActivity() {
    var DURATION = 30000
    var COUNTER = 5000

    val handler : Handler = Handler()
    lateinit var runnable : Runnable

    lateinit var t : Timer
    lateinit var tt : TimerTask

    var score = 0
    var problemNum = ""
    var answerNum = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_memory)

        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)

        runnable = Runnable {
            setButtonEnabledTrue()
            prog()
            test_memory_answer_and_problem_textview.text = "???"
            test_memory_skip_button.visibility = View.GONE
            test_memory_timer_progressbar.visibility = View.VISIBLE
            test_memory_answer_and_problem_textview.visibility = View.VISIBLE
            answerNum = ""
            test_memory_answer_and_problem_textview.animation = (null)
        }

        start()

        go_to_mainactivity_from_test_memory_activity_imageview.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
            finish()
        }

        test_memory_result_confirm_button.setOnClickListener {
            startActivity(Intent(this, TestActivity::class.java))
            finish()
        }

        test_num_one_button.setOnClickListener {
            answerNum += "1"
            test_memory_answer_and_problem_textview.text = answerNum
            if(answerNum == problemNum) start()
        }

        test_num_two_button.setOnClickListener {
            answerNum += "2"
            test_memory_answer_and_problem_textview.text = answerNum
            if(answerNum == problemNum) start()
        }
        test_num_three_button.setOnClickListener {
            answerNum += "3"
            test_memory_answer_and_problem_textview.text = answerNum
            if(answerNum == problemNum) start()
        }
        test_num_four_button.setOnClickListener {
            answerNum += "4"
            test_memory_answer_and_problem_textview.text = answerNum
            if(answerNum == problemNum) start()
        }
        test_num_five_button.setOnClickListener {
            answerNum += "5"
            test_memory_answer_and_problem_textview.text = answerNum
            if(answerNum == problemNum) start()
        }
        test_num_six_button.setOnClickListener {
            answerNum += "6"
            test_memory_answer_and_problem_textview.text = answerNum
            if(answerNum == problemNum) start()
        }
        test_num_seven_button.setOnClickListener {
            answerNum += "7"
            test_memory_answer_and_problem_textview.text = answerNum
            if(answerNum == problemNum) start()
        }
        test_num_eight_button.setOnClickListener {
            answerNum += "8"
            test_memory_answer_and_problem_textview.text = answerNum
            if(answerNum == problemNum) start()
        }
        test_num_nine_button.setOnClickListener {
            answerNum += "9"
            test_memory_answer_and_problem_textview.text = answerNum
            if(answerNum == problemNum) start()
        }
        test_num_zero_button.setOnClickListener {
            answerNum += "0"
            test_memory_answer_and_problem_textview.text = answerNum
            if(answerNum == problemNum) start()
        }

        test_backspace_button.setOnClickListener {
            if (answerNum.isEmpty() || answerNum.length == 1) {
                answerNum = ""
                test_memory_answer_and_problem_textview.text = "???"
            } else {
                answerNum = answerNum.substring(0, answerNum.length - 1)
                test_memory_answer_and_problem_textview.text = answerNum
            }
        }

        test_all_delete_button.setOnClickListener {
            answerNum = ""
            test_memory_answer_and_problem_textview.text = "???"
        }

        test_memory_skip_button.setOnClickListener {
            handler.removeCallbacks(runnable)
            setButtonEnabledTrue()
            prog()
            answerNum = ""
            test_memory_skip_button.visibility = View.GONE
            test_memory_timer_progressbar.visibility = View.VISIBLE
            test_memory_answer_and_problem_textview.text = "???"
            test_memory_answer_and_problem_textview.visibility = View.VISIBLE
            test_memory_answer_and_problem_textview.animation = (null)
        }


    }

    private fun startAlphaAnimation() {
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.interpolator = DecelerateInterpolator()
        fadeOut.duration = DURATION.toLong()

        val animation = AnimationSet(false)
        animation.addAnimation(fadeOut)
        test_memory_answer_and_problem_textview.animation = animation
    }

    private fun start() {
        score += 1
        problemNum = ""
        DURATION = if(DURATION >= 3000) (DURATION * 0.95).toInt() else 3000
        COUNTER = 10000
        setButtonEnabledFalse()
        test_memory_skip_button.visibility = View.VISIBLE
        test_memory_timer_progressbar.visibility = View.GONE
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
        test_memory_answer_and_problem_textview.text = problemNum
        test_memory_score_textview.text = score.toString()
        test_memory_result_textview.text = score.toString()
        startAlphaAnimation()
        try {
            t.cancel()
            tt.cancel()
        } catch (e : Exception) {
            Log.d("TAG", "testMemoryActivity - start : error is $e")
        }
        handler.run {
            postDelayed(runnable, DURATION.toLong())
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, TestActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun prog() {
        t = Timer()
        test_memory_timer_progressbar.max = COUNTER
        tt = object : TimerTask() {
            override fun run() {
                COUNTER -= 1
                test_memory_timer_progressbar.progress = COUNTER
                if((COUNTER / 100) == 0) {
                    t.cancel()
                    tt.cancel()
                    runOnUiThread {
                        test_memory_game_layout.visibility = View.GONE
                        test_memory_result_layout.visibility = View.VISIBLE
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
        val geniustestData = GetRoomMethod().getGeniusTestData(applicationContext)
        val uId = geniustestData.UniqueId
        Log.d("TAG", "score is $score, uId is $uId")
        geniusDataDifferenceApiService.getGeniusTestMemoryDifference(score.toString().toInt().toString(), uId).enqueue(object :
            Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "에러", Toast.LENGTH_LONG).show()
                Log.d("TAG", "postDataToServer testDifference error is $t")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    Log.d("TAG", "postDataToServer testDifference data is ${response.body()}")
                    val testMemoryDifference = response.body()!!.get("test_memory_difference")
                    if(testMemoryDifference.asString.isNotEmpty()) {
                        geniustestData.memoryScore = score.toString()
                        geniustestData.memoryDifference = testMemoryDifference.asString
                        UpdateRoomMethod().updateGeniusTestData(context = applicationContext, geniusTestData = geniustestData)
                    }
                } catch (e : Exception) {
                    Toast.makeText(applicationContext, "에러", Toast.LENGTH_LONG).show()
                    Log.d("TAG", "postDataToServer testDifference error is ${e.message}")
                }
            }

        })
    }

    private fun setButtonEnabledFalse() {
        test_num_one_button.isEnabled = false
        test_num_two_button.isEnabled = false
        test_num_three_button.isEnabled = false
        test_num_four_button.isEnabled = false
        test_num_five_button.isEnabled = false
        test_num_six_button.isEnabled = false
        test_num_seven_button.isEnabled = false
        test_num_eight_button.isEnabled = false
        test_num_nine_button.isEnabled = false
        test_num_zero_button.isEnabled = false
        test_backspace_button.isEnabled = false
        test_all_delete_button.isEnabled = false
    }

    private fun setButtonEnabledTrue() {
        test_num_one_button.isEnabled = true
        test_num_two_button.isEnabled = true
        test_num_three_button.isEnabled = true
        test_num_four_button.isEnabled = true
        test_num_five_button.isEnabled = true
        test_num_six_button.isEnabled = true
        test_num_seven_button.isEnabled = true
        test_num_eight_button.isEnabled = true
        test_num_nine_button.isEnabled = true
        test_num_zero_button.isEnabled = true
        test_backspace_button.isEnabled = true
        test_all_delete_button.isEnabled = true
    }
}