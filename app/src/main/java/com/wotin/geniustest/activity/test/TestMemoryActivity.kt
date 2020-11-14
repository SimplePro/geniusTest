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
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.google.android.gms.ads.*
import com.google.gson.JsonObject
import com.wotin.geniustest.R
import com.wotin.geniustest.databinding.ActivityTestMemoryBinding
import com.wotin.geniustest.retrofitBuilder.GeniusRetrofitBuilder.geniusDataDifferenceApiService
import com.wotin.geniustest.roomMethod.GetRoomMethod
import com.wotin.geniustest.roomMethod.UpdateRoomMethod
import com.wotin.geniustest.networkState
import com.wotin.geniustest.viewModel.GeniusTestViewModel
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

    var problemNum = ""
    var answerNum = ""
    
    lateinit var mBinding : ActivityTestMemoryBinding
    val geniusTestViewModel : GeniusTestViewModel by viewModels()

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this@TestMemoryActivity)

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-4792205746234657/2556864080"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdFailedToLoad(p0: LoadAdError?) {
                super.onAdFailedToLoad(p0)
                Log.e("TAG", "loaded error is $p0")
            }

            override fun onAdClosed() {
                super.onAdClosed()
                startActivity(Intent(this@TestMemoryActivity, TestActivity::class.java))
                finish()
            }
        }

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_test_memory)
        geniusTestViewModel.score.value = 0
        mBinding.viewModel = geniusTestViewModel
        mBinding.lifecycleOwner = this

        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)

        runnable = Runnable {
            setButtonEnabledTrue()
            prog()
            mBinding.testMemoryAnswerAndProblemTextview.text = "???"
            mBinding.testMemorySkipButton.visibility = View.GONE
            mBinding.testMemoryTimerProgressbar.visibility = View.VISIBLE
            mBinding.testMemoryAnswerAndProblemTextview.visibility = View.VISIBLE
            answerNum = ""
            mBinding.testMemoryAnswerAndProblemTextview.animation = (null)
        }

        start()

        mBinding.goToMainactivityFromTestMemoryActivityImageview.setOnClickListener {
            if(mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
                Log.d("TAG", "mInterstitialAd is Loaded")
            } else {
                startActivity(Intent(this@TestMemoryActivity, TestActivity::class.java))
                finish()
            }
        }

        mBinding.testMemoryResultConfirmButton.setOnClickListener {
            if(mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
                Log.d("TAG", "mInterstitialAd is Loaded")
            } else {
                startActivity(Intent(this@TestMemoryActivity, TestActivity::class.java))
                finish()
            }
        }

        mBinding.testNumOneButton.setOnClickListener {
            answerNum += "1"
            mBinding.testMemoryAnswerAndProblemTextview.text = answerNum
            if(answerNum == problemNum) start()
        }

        mBinding.testNumTwoButton.setOnClickListener {
            answerNum += "2"
            mBinding.testMemoryAnswerAndProblemTextview.text = answerNum
            if(answerNum == problemNum) start()
        }
        mBinding.testNumThreeButton.setOnClickListener {
            answerNum += "3"
            mBinding.testMemoryAnswerAndProblemTextview.text = answerNum
            if(answerNum == problemNum) start()
        }
        mBinding.testNumFourButton.setOnClickListener {
            answerNum += "4"
            mBinding.testMemoryAnswerAndProblemTextview.text = answerNum
            if(answerNum == problemNum) start()
        }
        mBinding.testNumFiveButton.setOnClickListener {
            answerNum += "5"
            mBinding.testMemoryAnswerAndProblemTextview.text = answerNum
            if(answerNum == problemNum) start()
        }
        mBinding.testNumSixButton.setOnClickListener {
            answerNum += "6"
            mBinding.testMemoryAnswerAndProblemTextview.text = answerNum
            if(answerNum == problemNum) start()
        }
        mBinding.testNumSevenButton.setOnClickListener {
            answerNum += "7"
            mBinding.testMemoryAnswerAndProblemTextview.text = answerNum
            if(answerNum == problemNum) start()
        }
        mBinding.testNumEightButton.setOnClickListener {
            answerNum += "8"
            mBinding.testMemoryAnswerAndProblemTextview.text = answerNum
            if(answerNum == problemNum) start()
        }
        mBinding.testNumNineButton.setOnClickListener {
            answerNum += "9"
            mBinding.testMemoryAnswerAndProblemTextview.text = answerNum
            if(answerNum == problemNum) start()
        }
        mBinding.testNumZeroButton.setOnClickListener {
            answerNum += "0"
            mBinding.testMemoryAnswerAndProblemTextview.text = answerNum
            if(answerNum == problemNum) start()
        }

        mBinding.testBackspaceButton.setOnClickListener {
            if (answerNum.isEmpty() || answerNum.length == 1) {
                answerNum = ""
                mBinding.testMemoryAnswerAndProblemTextview.text = "???"
            } else {
                answerNum = answerNum.substring(0, answerNum.length - 1)
                mBinding.testMemoryAnswerAndProblemTextview.text = answerNum
            }
        }

        mBinding.testAllDeleteButton.setOnClickListener {
            answerNum = ""
            mBinding.testMemoryAnswerAndProblemTextview.text = "???"
        }

        mBinding.testMemorySkipButton.setOnClickListener {
            handler.removeCallbacks(runnable)
            setButtonEnabledTrue()
            prog()
            answerNum = ""
            mBinding.testMemorySkipButton.visibility = View.GONE
            mBinding.testMemoryTimerProgressbar.visibility = View.VISIBLE
            mBinding.testMemoryAnswerAndProblemTextview.text = "???"
            mBinding.testMemoryAnswerAndProblemTextview.visibility = View.VISIBLE
            mBinding.testMemoryAnswerAndProblemTextview.animation = (null)
        }


    }

    private fun startAlphaAnimation() {
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.interpolator = DecelerateInterpolator()
        fadeOut.duration = DURATION.toLong()

        val animation = AnimationSet(false)
        animation.addAnimation(fadeOut)
        mBinding.testMemoryAnswerAndProblemTextview.animation = animation
    }

    private fun start() {
        geniusTestViewModel.plusScore()
        problemNum = ""
        DURATION = if(DURATION >= 3000) (DURATION * 0.95).toInt() else 3000
        COUNTER = 10000
        setButtonEnabledFalse()
        mBinding.testMemorySkipButton.visibility = View.VISIBLE
        mBinding.testMemoryTimerProgressbar.visibility = View.GONE
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
        mBinding.testMemoryAnswerAndProblemTextview.text = problemNum
        test_memory_score_textview.text = geniusTestViewModel.score.value!!.toString()
        test_memory_result_textview.text = geniusTestViewModel.score.value!!.toString()
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
        mBinding.testMemoryTimerProgressbar.max = COUNTER
        tt = object : TimerTask() {
            override fun run() {
                COUNTER -= 1
                mBinding.testMemoryTimerProgressbar.progress = COUNTER
                if((COUNTER / 100) == 0) {
                    t.cancel()
                    tt.cancel()
                    runOnUiThread {
                        test_memory_game_layout.visibility = View.GONE
                        test_memory_result_layout.visibility = View.VISIBLE
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
        mBinding.testNumOneButton.isEnabled = false
        mBinding.testNumTwoButton.isEnabled = false
        mBinding.testNumThreeButton.isEnabled = false
        mBinding.testNumFourButton.isEnabled = false
        mBinding.testNumFiveButton.isEnabled = false
        mBinding.testNumSixButton.isEnabled = false
        mBinding.testNumSevenButton.isEnabled = false
        mBinding.testNumEightButton.isEnabled = false
        mBinding.testNumNineButton.isEnabled = false
        mBinding.testNumZeroButton.isEnabled = false
        mBinding.testBackspaceButton.isEnabled = false
        mBinding.testAllDeleteButton.isEnabled = false
    }

    private fun setButtonEnabledTrue() {
        mBinding.testNumOneButton.isEnabled = true
        mBinding.testNumTwoButton.isEnabled = true
        mBinding.testNumThreeButton.isEnabled = true
        mBinding.testNumFourButton.isEnabled = true
        mBinding.testNumFiveButton.isEnabled = true
        mBinding.testNumSixButton.isEnabled = true
        mBinding.testNumSevenButton.isEnabled = true
        mBinding.testNumEightButton.isEnabled = true
        mBinding.testNumNineButton.isEnabled = true
        mBinding.testNumZeroButton.isEnabled = true
        mBinding.testBackspaceButton.isEnabled = true
        mBinding.testAllDeleteButton.isEnabled = true
    }
}