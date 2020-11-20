package com.wotin.geniustest.activity.test

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.*
import com.google.gson.JsonObject
import com.wotin.geniustest.*
import com.wotin.geniustest.R
import com.wotin.geniustest.adapter.test.TestConcentractionRecycelrViewAdapter
import com.wotin.geniustest.databinding.ActivityTestConcentractionBinding
import com.wotin.geniustest.retrofitBuilder.GeniusRetrofitBuilder.geniusDataDifferenceApiService
import com.wotin.geniustest.roomMethod.GetRoomMethod
import com.wotin.geniustest.roomMethod.UpdateRoomMethod
import com.wotin.geniustest.viewModel.GeniusTestViewModel
import kotlinx.android.synthetic.main.activity_test_concentraction.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*

class TestConcentractionActivity : AppCompatActivity(), TestConcentractionRecycelrViewAdapter.ItemClickListener {

    var counter = 5000
    var itemCount = 49
    var concentractionList : ArrayList<String> = arrayListOf()
    lateinit var concentractionRecyclerViewAdapter : TestConcentractionRecycelrViewAdapter
    lateinit var t : Timer
    lateinit var tt : TimerTask
    
    lateinit var mBinding : ActivityTestConcentractionBinding
    val geniusTestViewModel : GeniusTestViewModel by viewModels()

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storage = AppStorage(this)

        MobileAds.initialize(this@TestConcentractionActivity)

        mInterstitialAd = InterstitialAd(this)
//        test ads id : ca-app-pub-3940256099942544/1033173712
//        real ads id : ca-app-pub-4792205746234657/3766923849
        mInterstitialAd.adUnitId = "ca-app-pub-4792205746234657/3766923849"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdFailedToLoad(p0: LoadAdError?) {
                super.onAdFailedToLoad(p0)
                Log.e("TAG", "loaded error is $p0")
            }

            override fun onAdClosed() {
                super.onAdClosed()
                startActivity(Intent(this@TestConcentractionActivity, TestActivity::class.java))
                finish()
            }

        }

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_test_concentraction)
        mBinding.lifecycleOwner = this
        mBinding.viewModel = geniusTestViewModel

        mBinding.testConcentractionTimerProgressbar.min = 0

        setConcentractionList()

        val recyclerViewLayoutManager = GridLayoutManager(applicationContext, 10)
        test_concentraction_recyclerview.apply {
            layoutManager = recyclerViewLayoutManager
            adapter = concentractionRecyclerViewAdapter
            setHasFixedSize(true)
        }

        prog()

        mBinding.goToMainactivityFromTestConcentractionActivityImageview.setOnClickListener {
            if(!storage.purchasedNoAds()) {
                if(mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                    Log.d("TAG", "mInterstitialAd is Loaded")
                } else {
                    startActivity(Intent(this@TestConcentractionActivity, TestActivity::class.java))
                    finish()
                }
            } else {
                startActivity(Intent(this@TestConcentractionActivity, TestActivity::class.java))
                finish()
            }
        }

        mBinding.testConcentractionResultConfirmButton.setOnClickListener {
            if(!storage.purchasedNoAds()) {
                if(mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                    Log.d("TAG", "mInterstitialAd is Loaded")
                } else {
                    startActivity(Intent(this@TestConcentractionActivity, TestActivity::class.java))
                    finish()
                }
            } else {
                startActivity(Intent(this@TestConcentractionActivity, TestActivity::class.java))
                finish()
            }
        }

    }


    private fun restart() {
        geniusTestViewModel.plusScore()
        if(geniusTestViewModel.score.value!! % 5 == 0) {
            resetRecyclerViewGridLayout()
        }
        setConcentractionList()
        t.cancel()
        tt.cancel()
        counter = if(counter >= 200) (counter * 0.95).toInt() else 200
        prog()
    }

    private fun prog() {
        t = Timer()
        mBinding.testConcentractionTimerProgressbar.max = counter
        tt = object : TimerTask() {
            override fun run() {
                counter -= 1
                mBinding.testConcentractionTimerProgressbar.progress = counter
                if((counter / 100) == 0) {
                    t.cancel()
                    tt.cancel()
                    runOnUiThread {
                        mBinding.testConcentractionGameLayout.visibility = View.GONE
                        mBinding.testConcentractionResultLayout.visibility = View.VISIBLE
                        val connectivityManager : ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                        if(networkState(connectivityManager)) {
                            postDataToServer(score = geniusTestViewModel.score.value!!)
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
            TestConcentractionRecycelrViewAdapter(
                concentractionList,
                this
            )
        test_concentraction_recyclerview.apply {
            adapter = concentractionRecyclerViewAdapter
            setHasFixedSize(true)
        }
    }

    private fun resetRecyclerViewGridLayout() {
        when {
            geniusTestViewModel.score.value!! >= 20 -> {
                itemCount = 74
                val recyclerViewLayoutManager = GridLayoutManager(applicationContext, 15)
                test_concentraction_recyclerview.layoutManager = recyclerViewLayoutManager
            }
            geniusTestViewModel.score.value!! >= 15 -> {
                itemCount = 64
                val recyclerViewLayoutManager = GridLayoutManager(applicationContext, 13)
                test_concentraction_recyclerview.layoutManager = recyclerViewLayoutManager
            }
            geniusTestViewModel.score.value!! >= 10 -> {
                itemCount = 59
                val recyclerViewLayoutManager = GridLayoutManager(applicationContext, 12)
                test_concentraction_recyclerview.layoutManager = recyclerViewLayoutManager
            } else -> {
            itemCount = 49
        }
        }
        concentractionRecyclerViewAdapter.notifyDataSetChanged()
        test_concentraction_recyclerview.apply {
            adapter = concentractionRecyclerViewAdapter
            setHasFixedSize(true)
        }
    }

    private fun postDataToServer(score : Int) {
        val geniusTestData = GetRoomMethod().getGeniusTestData(applicationContext)
        val uId = geniusTestData.UniqueId
        Log.d("TAG", "geniusTestViewModel.score.value!! is $geniusTestViewModel.score.value!!, uId is $uId")
        geniusDataDifferenceApiService.getGeniusTestConcentractionDifference(geniusTestViewModel.score.value!!.toString(), uId).enqueue(object :
            Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "에러", Toast.LENGTH_LONG).show()
                Log.d("TAG", "postDataToServer testDifference error is $t")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    Log.d("TAG", "postDataToServer testDifference data is ${response.body()}")
                    val testConcentractionDifference = response.body()!!.get("test_concentraction_difference")
                    Log.d("TAG", "postDataToServer testConcentractionDifference is $testConcentractionDifference, geniusTestViewModel.score.value!! is $geniusTestViewModel.score.value!!")
                    if(testConcentractionDifference.asString.isNotEmpty()) {
                        geniusTestData.concentractionScore = geniusTestViewModel.score.value!!.toString()
                        geniusTestData.concentractionDifference = testConcentractionDifference.asString
                        UpdateRoomMethod().updateGeniusTestData(context = applicationContext, geniusTestData = geniusTestData)
                        Log.d("TAG", "postDataToServer testConcentractionDifference is Not Empty, getGeniusTestData is ${GetRoomMethod().getGeniusTestData(applicationContext)}")
                    }
                } catch (e : Exception) {
                    Toast.makeText(applicationContext, "에러", Toast.LENGTH_LONG).show()
                    Log.d("TAG", "postDataToServer testDifference error is ${e.message}")
                }
            }

        })
    }
}