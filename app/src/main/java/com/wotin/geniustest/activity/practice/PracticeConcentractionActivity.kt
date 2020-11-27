package com.wotin.geniustest.activity.practice

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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonObject
import com.wotin.geniustest.adapter.practice.PracticeConcentractionRecyclerViewAdapter
import com.wotin.geniustest.R
import com.wotin.geniustest.databinding.ActivityPracticeConcentractionBinding
import com.wotin.geniustest.retrofitBuilder.GeniusRetrofitBuilder.geniusDataDifferenceApiService
import com.wotin.geniustest.roomMethod.GetRoomMethod
import com.wotin.geniustest.roomMethod.UpdateRoomMethod
import com.wotin.geniustest.networkState
import com.wotin.geniustest.viewModel.GeniusTestViewModel
import kotlinx.android.synthetic.main.activity_practice_concentraction.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class PracticeConcentractionActivity : AppCompatActivity(), PracticeConcentractionRecyclerViewAdapter.ItemClickListener {

    var counter = 5000
    var itemCount = 49
    var concentractionList : ArrayList<String> = arrayListOf()
    lateinit var concentractionRecyclerViewAdapter : PracticeConcentractionRecyclerViewAdapter
    lateinit var t : Timer
    lateinit var tt : TimerTask

    lateinit var mBinding : ActivityPracticeConcentractionBinding
    val geniusTestViewModel: GeniusTestViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_practice_concentraction)
        mBinding.lifecycleOwner = this
        mBinding.viewModel = geniusTestViewModel

        mBinding.practiceConcentractionTimerProgressbar.min = 0

        setConcentractionList()

        val recyclerViewLayoutManager = GridLayoutManager(applicationContext, 10)
        mBinding.practiceConcentractionRecyclerview.apply {
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
        mBinding.practiceConcentractionTimerProgressbar.max = counter
        tt = object : TimerTask() {
            override fun run() {
                counter -= 1
                mBinding.practiceConcentractionTimerProgressbar.progress = counter
                if(counter <= 0) {
                    t.cancel()
                    tt.cancel()
                    runOnUiThread {
                        practice_concentraction_game_layout.visibility = View.GONE
                        practice_concentraction_result_layout.visibility = View.VISIBLE
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
        mBinding.practiceConcentractionRecyclerview.apply {
            adapter = concentractionRecyclerViewAdapter
            setHasFixedSize(true)
        }
    }

    private fun resetRecyclerViewGridLayout() {
        when {
            geniusTestViewModel.score.value!! >= 20 -> {
                itemCount = 74
                val recyclerViewLayoutManager = GridLayoutManager(applicationContext, 15)
                mBinding.practiceConcentractionRecyclerview.layoutManager = recyclerViewLayoutManager
            }
            geniusTestViewModel.score.value!! >= 15 -> {
                itemCount = 64
                val recyclerViewLayoutManager = GridLayoutManager(applicationContext, 13)
                mBinding.practiceConcentractionRecyclerview.layoutManager = recyclerViewLayoutManager
            }
            geniusTestViewModel.score.value!! >= 10 -> {
                itemCount = 59
                val recyclerViewLayoutManager = GridLayoutManager(applicationContext, 12)
                mBinding.practiceConcentractionRecyclerview.layoutManager = recyclerViewLayoutManager
            } else -> {
            itemCount = 49
        }
        }
        concentractionRecyclerViewAdapter.notifyDataSetChanged()
        mBinding.practiceConcentractionRecyclerview.apply {
            adapter = concentractionRecyclerViewAdapter
            setHasFixedSize(true)
        }
    }

    private fun postDataToServer(score : Int) {
        val geniusPracticeData = GetRoomMethod().getGeniusPracticeData(applicationContext)
        val uId = geniusPracticeData.UniqueId
        Log.d("TAG", "geniusTestViewModel.score.value!! is $geniusTestViewModel.score.value!!, uId is $uId")
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
                        geniusPracticeData.concentractionScore = geniusTestViewModel.score.value!!.toString()
                        geniusPracticeData.concentractionDifference = practiceConcentractionDifference.asString
                        UpdateRoomMethod().updateGeniusPracticeData(context = applicationContext, geniusPracticeData = geniusPracticeData)
                    }
                } catch (e : Exception) {
                    Toast.makeText(applicationContext, "에러", Toast.LENGTH_LONG).show()
                    Log.d("TAG", "postDataToServer PracticeDifference error is ${e.message}")
                }
            }

        })
    }

}