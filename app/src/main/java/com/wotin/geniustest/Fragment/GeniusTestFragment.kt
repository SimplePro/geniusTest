package com.wotin.geniustest.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.JsonObject
import com.wotin.geniustest.Activity.Test.TestActivity
import com.wotin.geniustest.CustomClass.GeniusTest.GeniusTestDataCustomClass
import com.wotin.geniustest.R
import com.wotin.geniustest.RetrofitInterface.Genius.RetrofitAboutGeniusData
import com.wotin.geniustest.RoomMethod.GetRoomMethod
import com.wotin.geniustest.RoomMethod.UserRoomMethod
import kotlinx.android.synthetic.main.fragment_genius_test.*
import kotlinx.android.synthetic.main.fragment_genius_test.view.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class GeniusTestFragment : Fragment() {

    lateinit var geniusTestData : GeniusTestDataCustomClass

    lateinit var retrofit: Retrofit
    lateinit var geniusDataDifferenceApiService: RetrofitAboutGeniusData
    lateinit var okHttpClient: OkHttpClient
    val baseUrl = "http://118.32.174.85:8080"

    lateinit var testSumDifference : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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

        getTestSumDifference()

        val rootView = inflater.inflate(R.layout.fragment_genius_test, container, false)
        geniusTestData = GetRoomMethod().getGeniusTestData(activity!!.applicationContext)

        rootView.genius_test_level_textview.text = geniusTestData.level

        rootView.genius_test_play_imageView.setOnClickListener {
            val intent = Intent(requireContext(), TestActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }

        // Inflate the layout for this fragment
        return rootView
    }

    private fun getTestSumDifference() {
        val userData = UserRoomMethod().getUserData(activity!!.applicationContext)
        geniusDataDifferenceApiService.getGeniusTestSumDifference(pk = userData.UniqueId).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("TAG", "onFailure: getTestSumDifference error is $t")
                Toast.makeText(activity!!.applicationContext, "에러", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                testSumDifference = response.body()!!.get("test_sum_difference").asString
                genius_test_all_difference_textview.text = testSumDifference
            }
        })
    }

}