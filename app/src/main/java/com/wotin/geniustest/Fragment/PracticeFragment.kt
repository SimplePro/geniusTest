package com.wotin.geniustest.Fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.google.gson.JsonObject
import com.wotin.geniustest.Activity.PracticeActivity
import com.wotin.geniustest.CustomClass.GeniusPractice.GeniusPracticeDataCustomClass
import com.wotin.geniustest.DB.GeniusPracticeDataDB
import com.wotin.geniustest.R
import com.wotin.geniustest.RetrofitInterface.RetrofitAboutGeniusData
import com.wotin.geniustest.getGeniusPracticeData
import com.wotin.geniustest.networkState
import com.wotin.geniustest.updateGeniusPracticeData
import kotlinx.android.synthetic.main.fragment_practice.view.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit

class PracticeFragment : Fragment() {

    lateinit var geniusPracticeData : GeniusPracticeDataCustomClass
    lateinit var retrofit: Retrofit
    lateinit var getGeniusDataDifferenceApiService: RetrofitAboutGeniusData
    lateinit var okHttpClient: OkHttpClient
    val baseUrl = "http://220.72.174.101:8080"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_practice, container, false)

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
        getGeniusDataDifferenceApiService = retrofit.create(RetrofitAboutGeniusData::class.java)
        geniusPracticeData = getGeniusPracticeData(activity!!.applicationContext)


        val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(networkState(connectivityManager)){
            getGeniusDataDifferenceApiService.getGeniusPracticeDifference(geniusPracticeData.concentractionScore, geniusPracticeData.UniqueId).enqueue(object :
                Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(activity!!.applicationContext, "에러", Toast.LENGTH_LONG).show()
                    Log.d("TAG", "postDataToServer PracticeDifference error is $t")
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    try {
                        Log.d("TAG", "postDataToServer PracticeDifference data is ${response.body()}")
                        val practiceConcentractionDifference = response.body()!!.get("practice_concentraction_difference")
                        geniusPracticeData.concentractionDifference = practiceConcentractionDifference.asString
                        updateGeniusPracticeData(context = activity!!.applicationContext, geniusPracticeData = geniusPracticeData)
                    } catch (e : Exception) {
                        Toast.makeText(activity!!.applicationContext, "에러", Toast.LENGTH_LONG).show()
                        Log.d("TAG", "postDataToServer PracticeDifference error is ${e.message}")
                    }
                }

            })
        } else {
            Toast.makeText(activity!!.applicationContext, "네트워크 연결 실패", Toast.LENGTH_LONG).show()
        }

        geniusPracticeData = getGeniusPracticeData(activity!!.applicationContext)

        Log.d("TAG", "((geniusPracticeData.concentractionDifference.toFloat() + geniusPracticeData.memoryDifference.toFloat() / 2).toString()) is " +
                (((geniusPracticeData.concentractionDifference.toFloat() + geniusPracticeData.memoryDifference.toFloat()) / 2.0f).toString()))

        var allDifference = (((geniusPracticeData.concentractionDifference.toFloat() + geniusPracticeData.memoryDifference.toFloat()) / 2.0f))
        rootView.practice_all_difference_textview.text = String.format("%.2f", allDifference)

        rootView.practice_play_imageView.setOnClickListener {
            val intent = Intent(requireContext(), PracticeActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }

        return rootView
    }



}