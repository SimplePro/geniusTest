package com.wotin.geniustest.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.JsonObject
import com.wotin.geniustest.activity.test.TestActivity
import com.wotin.geniustest.customClass.geniusTest.GeniusTestDataCustomClass
import com.wotin.geniustest.R
import com.wotin.geniustest.retrofitBuilder.GeniusRetrofitBuilder
import com.wotin.geniustest.retrofitInterface.genius.RetrofitAboutGeniusData
import com.wotin.geniustest.roomMethod.GetRoomMethod
import com.wotin.geniustest.roomMethod.UserRoomMethod
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

    lateinit var testSumDifference : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        GeniusRetrofitBuilder.geniusDataDifferenceApiService.getGeniusTestSumDifference(pk = userData.UniqueId).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("TAG", "onFailure: getTestSumDifference error is $t")
                Toast.makeText(activity!!.applicationContext, "에러", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                testSumDifference = response.body()!!.get("test_sum_difference").asString
                Log.d("TAG", "GeniustestFragment : getTestSumDifference - testSumDifference is ${testSumDifference.toFloat()}")
                genius_test_all_difference_textview.text = testSumDifference    
                when {
                    testSumDifference.toFloat() < 0.3 -> genius_test_level_textview.text = "천재"
                    testSumDifference.toFloat() < 10 -> genius_test_level_textview.text = "고수"
                    testSumDifference.toFloat() < 50 -> genius_test_level_textview.text = "중수"
                    else -> genius_test_level_textview.text = "초보"
                }
            }
        })
    }

}