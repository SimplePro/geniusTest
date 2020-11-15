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
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.*
import com.google.gson.JsonObject
import com.wotin.geniustest.*
import com.wotin.geniustest.R
import com.wotin.geniustest.adapter.test.TestQuicknessRecyclerViewAdapter
import com.wotin.geniustest.databinding.ActivityTestQuicknessBinding
import com.wotin.geniustest.retrofitBuilder.GeniusRetrofitBuilder.geniusDataDifferenceApiService
import com.wotin.geniustest.roomMethod.GetRoomMethod
import com.wotin.geniustest.roomMethod.UpdateRoomMethod
import com.wotin.geniustest.viewModel.GeniusTestViewModel
import kotlinx.android.synthetic.main.activity_test_quickness.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class TestQuicknessActivity : AppCompatActivity(), TestQuicknessRecyclerViewAdapter.ItemClickListener {

    var counter = 5000
    var quicknessList : ArrayList<String> = arrayListOf()
    var currentColor = ""
    lateinit var quicknessRecyclerViewAdapter : TestQuicknessRecyclerViewAdapter
    lateinit var t : Timer
    lateinit var tt : TimerTask

    var brainMode = "right_brain"
    
    lateinit var mBinding : ActivityTestQuicknessBinding
    val geniusTestViewModel : GeniusTestViewModel by viewModels()

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_test_quickness)
        mBinding.viewModel = geniusTestViewModel
        mBinding.lifecycleOwner = this

        MobileAds.initialize(this@TestQuicknessActivity)

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-4792205746234657/3766923849"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener() {

            override fun onAdFailedToLoad(p0: LoadAdError?) {
                super.onAdFailedToLoad(p0)
                Log.e("TAG", "loaded error is $p0")
            }

            override fun onAdClosed() {
                super.onAdClosed()
                startActivity(Intent(this@TestQuicknessActivity, TestActivity::class.java))
                finish()
            }
        }

        if(intent.hasExtra("brain_mode")) brainMode = intent.getStringExtra("brain_mode")!!

        setQuicknessList()
        quicknessRecyclerViewAdapter = TestQuicknessRecyclerViewAdapter(quicknessList, this)

        mBinding.testQuicknessRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@TestQuicknessActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = quicknessRecyclerViewAdapter
            setHasFixedSize(true)
        }

        prog()

        mBinding.goToMainactivityFromTestQuicknessActivityImageview.setOnClickListener {
            if(mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
                Log.d("TAG", "mInterstitialAd is Loaded")
            } else {
                Log.d("TAG", "mInterstitialAd is not Loaded")
                startActivity(Intent(this, TestActivity::class.java))
                finish()
            }
        }

        mBinding.testQuicknessResultConfirmButton.setOnClickListener {
            if(mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
                Log.d("TAG", "mInterstitialAd is Loaded")
            } else {
                Log.d("TAG", "mInterstitialAd is not Loaded")
                startActivity(Intent(this, TestActivity::class.java))
                finish()
            }
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
        test_quickness_timer_progressbar.max = counter
        tt = object : TimerTask() {
            override fun run() {
                counter -= 1
                test_quickness_timer_progressbar.progress = counter
                if((counter / 100) == 0) {
                    t.cancel()
                    tt.cancel()
                    runOnUiThread {
                        mBinding.testQuicknessGameLayout.visibility = View.GONE
                        mBinding.testQuicknessResultLayout.visibility = View.VISIBLE
                        val connectivityManager : ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                        if(networkState(connectivityManager)) {
                            Log.d("TAG  ", "run: score is ${geniusTestViewModel.score.value!!}")
                            postDataToServer(geniusTestViewModel.score.value!!)
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
                mBinding.testQuicknessColorTextview.setTextColor(resources.getColor(R.color.colorRed))
            }
            "주황" -> {
                mBinding.testQuicknessColorTextview.setTextColor(resources.getColor(R.color.colorOrange))
            }
            "노랑" -> {
                mBinding.testQuicknessColorTextview.setTextColor(resources.getColor(R.color.colorYellow))
            }
            "연두" -> {
                mBinding.testQuicknessColorTextview.setTextColor(resources.getColor(R.color.colorLightGreen))
            }
            "초록" -> {
                mBinding.testQuicknessColorTextview.setTextColor(resources.getColor(R.color.colorGreen))
            }
            "하늘" -> {
                mBinding.testQuicknessColorTextview.setTextColor(resources.getColor(R.color.colorSkyBlue))
            }
            "파랑" -> {
                mBinding.testQuicknessColorTextview.setTextColor(resources.getColor(R.color.colorBlue))
            }
            "보라" -> {
                mBinding.testQuicknessColorTextview.setTextColor(resources.getColor(R.color.colorPurple))
            }
        }
        val fakeColorList = mutableListOf<String>("빨강", "주황", "노랑", "연두", "초록", "하늘", "파랑", "보라")
        fakeColorList.remove(currentColor)
        val fakeColor = fakeColorList.random()
        mBinding.testQuicknessColorTextview.text = fakeColor
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
        mBinding.testQuicknessRecyclerview.apply {
            adapter = quicknessRecyclerViewAdapter
            setHasFixedSize(true)
        }
    }

    override fun itemClick(clickedColor: String) {
        if(clickedColor == currentColor && brainMode == "right_brain") restart()
        else if (clickedColor != currentColor && brainMode == "left_brain") restart()
        else {
            val vib = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vib.vibrate(500)
            counter -= 100
        }
    }
}