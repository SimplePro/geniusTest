package com.wotin.geniustest.activity.practice

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
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.google.gson.JsonObject
import com.wotin.geniustest.R
import com.wotin.geniustest.databinding.ActivityPracticeMemoryBinding
import com.wotin.geniustest.retrofitBuilder.GeniusRetrofitBuilder.geniusDataDifferenceApiService
import com.wotin.geniustest.roomMethod.GetRoomMethod
import com.wotin.geniustest.roomMethod.UpdateRoomMethod
import com.wotin.geniustest.networkState
import com.wotin.geniustest.viewModel.GeniusTestViewModel
import kotlinx.android.synthetic.main.activity_practice_memory.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*

class PracticeMemoryActivity : AppCompatActivity() {

    var DURATION = 30000
    var COUNTER = 5000

    val handler : Handler = Handler()
    lateinit var runnable : Runnable

    lateinit var t : Timer
    lateinit var tt : TimerTask

    var problemNum = ""
    var answerNum = ""
    
    lateinit var mBinding : ActivityPracticeMemoryBinding
    val geniusTestViewModel : GeniusTestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_practice_memory)
        geniusTestViewModel.score.value = 0
        mBinding.viewModel = geniusTestViewModel
        mBinding.lifecycleOwner = this

        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)

        runnable = Runnable {
            setButtonEnabledTrue()
            prog()
            mBinding.practiceMemoryAnswerAndProblemTextview.text = "???"
            mBinding.practiceMemorySkipButton.visibility = View.GONE
            mBinding.practiceMemoryTimerProgressbar.visibility = View.VISIBLE
            mBinding.practiceMemoryAnswerAndProblemTextview.visibility = View.VISIBLE
            answerNum = ""
            mBinding.practiceMemoryAnswerAndProblemTextview.animation = (null)
        }

        start()

        mBinding.goToMainactivityFromPracticeMemoryActivityImageview.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            startActivity(intent)
            finish()
        }

        mBinding.practiceMemoryResultConfirmButton.setOnClickListener {
            startActivity(Intent(this, PracticeActivity::class.java))
            finish()
        }

        mBinding.practiceNumOneButton.setOnClickListener {
            answerNum += "1"
            mBinding.practiceMemoryAnswerAndProblemTextview.text = answerNum
            if(answerNum == problemNum) start()
        }

        mBinding.practiceNumTwoButton.setOnClickListener {
            answerNum += "2"
            mBinding.practiceMemoryAnswerAndProblemTextview.text = answerNum
            if(answerNum == problemNum) start()
        }
        mBinding.practiceNumThreeButton.setOnClickListener {
            answerNum += "3"
            mBinding.practiceMemoryAnswerAndProblemTextview.text = answerNum
            if(answerNum == problemNum) start()
        }
        mBinding.practiceNumFourButton.setOnClickListener {
            answerNum += "4"
            mBinding.practiceMemoryAnswerAndProblemTextview.text = answerNum
            if(answerNum == problemNum) start()
        }
        mBinding.practiceNumFiveButton.setOnClickListener {
            answerNum += "5"
            mBinding.practiceMemoryAnswerAndProblemTextview.text = answerNum
            if(answerNum == problemNum) start()
        }
        mBinding.practiceNumSixButton.setOnClickListener {
            answerNum += "6"
            mBinding.practiceMemoryAnswerAndProblemTextview.text = answerNum
            if(answerNum == problemNum) start()
        }
        mBinding.practiceNumSevenButton.setOnClickListener {
            answerNum += "7"
            mBinding.practiceMemoryAnswerAndProblemTextview.text = answerNum
            if(answerNum == problemNum) start()
        }
        mBinding.practiceNumEightButton.setOnClickListener {
            answerNum += "8"
            mBinding.practiceMemoryAnswerAndProblemTextview.text = answerNum
            if(answerNum == problemNum) start()
        }
        mBinding.practiceNumNineButton.setOnClickListener {
            answerNum += "9"
            mBinding.practiceMemoryAnswerAndProblemTextview.text = answerNum
            if(answerNum == problemNum) start()
        }
        mBinding.practiceNumZeroButton.setOnClickListener {
            answerNum += "0"
            mBinding.practiceMemoryAnswerAndProblemTextview.text = answerNum
            if(answerNum == problemNum) start()
        }

        mBinding.practiceBackspaceButton.setOnClickListener {
            if (answerNum.isEmpty() || answerNum.length == 1) {
                answerNum = ""
                mBinding.practiceMemoryAnswerAndProblemTextview.text = "???"
            } else {
                answerNum = answerNum.substring(0, answerNum.length - 1)
                mBinding.practiceMemoryAnswerAndProblemTextview.text = answerNum
            }
        }

        mBinding.practiceAllDeleteButton.setOnClickListener {
            answerNum = ""
            mBinding.practiceMemoryAnswerAndProblemTextview.text = "???"
        }

        mBinding.practiceMemorySkipButton.setOnClickListener {
            handler.removeCallbacks(runnable)
            setButtonEnabledTrue()
            prog()
            answerNum = ""
            mBinding.practiceMemorySkipButton.visibility = View.GONE
            mBinding.practiceMemoryTimerProgressbar.visibility = View.VISIBLE
            mBinding.practiceMemoryAnswerAndProblemTextview.text = "???"
            mBinding.practiceMemoryAnswerAndProblemTextview.visibility = View.VISIBLE
            mBinding.practiceMemoryAnswerAndProblemTextview.animation = (null)
        }


    }

    private fun startAlphaAnimation() {
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.interpolator = DecelerateInterpolator()
        fadeOut.duration = DURATION.toLong()

        val animation = AnimationSet(false)
        animation.addAnimation(fadeOut)
        mBinding.practiceMemoryAnswerAndProblemTextview.animation = animation
    }

    private fun start() {
        geniusTestViewModel.plusScore()
        problemNum = ""
        DURATION = if(DURATION >= 3000) (DURATION * 0.95).toInt() else 3000
        COUNTER = 10000
        setButtonEnabledFalse()
        mBinding.practiceMemorySkipButton.visibility = View.VISIBLE
        mBinding.practiceMemoryTimerProgressbar.visibility = View.GONE
        when {
            geniusTestViewModel.score.value!! >= 20 -> {
                for(i in 0 .. 12) {
                    problemNum += Random().nextInt(9).toString()
                }
            }
            geniusTestViewModel.score.value!! >= 10 -> {
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
        mBinding.practiceMemoryAnswerAndProblemTextview.text = problemNum
        practice_memory_score_textview.text = geniusTestViewModel.score.value!!.toString()
        practice_memory_result_textview.text = geniusTestViewModel.score.value!!.toString()
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
        mBinding.practiceMemoryTimerProgressbar.max = COUNTER
        tt = object : TimerTask() {
            override fun run() {
                COUNTER -= 1
                mBinding.practiceMemoryTimerProgressbar.progress = COUNTER
                if(COUNTER <= 0) {
                    t.cancel()
                    tt.cancel()
                    runOnUiThread {
                        practice_memory_game_layout.visibility = View.GONE
                        practice_memory_result_layout.visibility = View.VISIBLE
                        val connectivityManager : ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                        if(networkState(connectivityManager)) {
                            Log.d("TAG  ", "run: score is $geniusTestViewModel.score.value!!")
                            postDataToServer(geniusTestViewModel.score.value!!)
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
        mBinding.practiceNumOneButton.isEnabled = false
        mBinding.practiceNumTwoButton.isEnabled = false
        mBinding.practiceNumThreeButton.isEnabled = false
        mBinding.practiceNumFourButton.isEnabled = false
        mBinding.practiceNumFiveButton.isEnabled = false
        mBinding.practiceNumSixButton.isEnabled = false
        mBinding.practiceNumSevenButton.isEnabled = false
        mBinding.practiceNumEightButton.isEnabled = false
        mBinding.practiceNumNineButton.isEnabled = false
        mBinding.practiceNumZeroButton.isEnabled = false
        mBinding.practiceBackspaceButton.isEnabled = false
        mBinding.practiceAllDeleteButton.isEnabled = false
    }

    private fun setButtonEnabledTrue() {
        mBinding.practiceNumOneButton.isEnabled = true
        mBinding.practiceNumTwoButton.isEnabled = true
        mBinding.practiceNumThreeButton.isEnabled = true
        mBinding.practiceNumFourButton.isEnabled = true
        mBinding.practiceNumFiveButton.isEnabled = true
        mBinding.practiceNumSixButton.isEnabled = true
        mBinding.practiceNumSevenButton.isEnabled = true
        mBinding.practiceNumEightButton.isEnabled = true
        mBinding.practiceNumNineButton.isEnabled = true
        mBinding.practiceNumZeroButton.isEnabled = true
        mBinding.practiceBackspaceButton.isEnabled = true
        mBinding.practiceAllDeleteButton.isEnabled = true
    }

}