package com.wotin.geniustest.Activity.UserManagement

import android.animation.ValueAnimator
import android.content.Intent
import android.net.http.HttpResponseCache
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserManager
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.wotin.geniustest.Activity.MainActivity
import com.wotin.geniustest.Converters.MapJsonConverter
import com.wotin.geniustest.EncryptionAndDetoxification
import com.wotin.geniustest.R
import com.wotin.geniustest.RetrofitInterface.Genius.RetrofitPostUserHeart
import com.wotin.geniustest.RetrofitInterface.User.RetrofitSearchUserData
import com.wotin.geniustest.RoomMethod.GetRoomMethod
import com.wotin.geniustest.RoomMethod.UserRoomMethod
import kotlinx.android.synthetic.main.activity_user_information.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class UserInformationActivity : AppCompatActivity() {

    private var isHearted = false
    private var userId = ""
    private var currentSeeUserUID = ""

    lateinit var retrofit: Retrofit
    lateinit var searchUserDataApiService: RetrofitSearchUserData
    lateinit var postUserHeart : RetrofitPostUserHeart
    lateinit var okHttpClient: OkHttpClient
    val baseUrl = "http://220.117.41.156:8080"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_information)

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
        searchUserDataApiService = retrofit.create(RetrofitSearchUserData::class.java)
        postUserHeart = retrofit.create(RetrofitPostUserHeart::class.java)

        if(intent.hasExtra("userId")) {
            userId = intent.getStringExtra("userId")
            Log.d("TAG", "onCreate: UniqueId is $userId")
            getUserDataFromServer(userId)
        }

        search_user_information_button.setOnClickListener {
            isHearted = false
            if(search_user_information_edittext.text.isNotEmpty()) {
                userId = EncryptionAndDetoxification().encryptionAndDetoxification(search_user_information_edittext.text.toString())
                getUserDataFromServer(userId)
            } else {
                userId = UserRoomMethod().getUserData(applicationContext).id
                getUserDataFromServer(userId)
            }
        }

        refresh_layout_among_user_information.setOnRefreshListener {
            isHearted = false
            getUserDataFromServer(userId)
            refresh_layout_among_user_information.isRefreshing = false
        }

        user_heart_lottieanimation_among_user_information.setOnClickListener {
            if(currentSeeUserUID == UserRoomMethod().getUserData(applicationContext).UniqueId) {
                Toast.makeText(applicationContext, "자기 자신에게는 하트를 할 수 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                if(isHearted) {
                    minusHeart()
                    user_heart_text_view_among_user_information.text = (user_heart_text_view_among_user_information.text.toString().toInt() - 1).toString()
                }
                else if(!isHearted){
                    user_heart_text_view_among_user_information.text = (user_heart_text_view_among_user_information.text.toString().toInt() + 1).toString()
                    plusHeart()
                }
                isHearted = !isHearted
                setHeartLottieAnimation()
            }
        }

        go_to_mainactivity_from_user_information_activity_imageview.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getUserDataFromServer(id : String) {
        searchUserDataApiService.getUserData(id).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "에러", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "onFailure: error is $t")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val getData = response.body()!!
                Log.d("TAG", "onResponse: getData is $getData")
                if(getData["name"].asString == "") {
                    Toast.makeText(applicationContext, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show()
                } else {
                    val testData = MapJsonConverter().MapToJsonConverter(getData["test"].toString())
                    val practiceData = MapJsonConverter().MapToJsonConverter(getData["practice"].toString())
                    currentSeeUserUID = getData["uniqueId"].asString
                    setTheUI(name = getData["name"].asString,
                            id = getData["id"].asString,
                            heart = getData["peopleIdHeartToMe"].asJsonArray,
                            practiceMemoryScore = practiceData["practice_memory_score"].toString(),
                            practiceMemoryDifference = practiceData["practice_memory_difference"].toString(),
                            practiceConcentractionScore = practiceData["practice_concentraction_score"].toString(),
                            practiceConcentractionDifference = practiceData["practice_concentraction_difference"].toString(),
                            practiceQuicknessScore = practiceData["practice_quickness_score"].toString(),
                            practiceQuicknessDifference = practiceData["practice_quickness_difference"].toString(),
                            testMemoryScore = testData["test_memory_score"].toString(),
                            testMemoryDifference = testData["test_memory_difference"].toString(),
                            testConcentractionScore = testData["test_concentraction_score"].toString(),
                            testConcentractionDifference = testData["test_concentraction_difference"].toString(),
                            testQuicknessScore = testData["test_quickness_score"].toString(),
                            testQuicknessDifference = testData["test_quickness_difference"].toString(),
                            level = getData["level"].asString)
                }
            }

        })
    }

    private fun setTheUI(name : String, id : String,
                         heart: JsonArray,
                         practiceMemoryScore : String, practiceMemoryDifference : String,
                         practiceConcentractionScore : String, practiceConcentractionDifference : String,
                         practiceQuicknessScore : String, practiceQuicknessDifference : String,
                         testMemoryScore : String, testMemoryDifference : String,
                         testConcentractionScore : String, testConcentractionDifference : String,
                         testQuicknessScore : String, testQuicknessDifference : String,
                         level : String
                         ) {
        user_name_text_view_among_user_information.text = name
        user_id_text_view_among_user_information.text = EncryptionAndDetoxification().encryptionAndDetoxification(id)
        user_heart_text_view_among_user_information.text = heart.toList().size.toString()

        if(currentSeeUserUID != UserRoomMethod().getUserData(applicationContext).UniqueId) {
            for(i in heart.toList()) {
                if(i.asString == UserRoomMethod().getUserData(applicationContext).UniqueId) {
                    Log.d("TAG", "setTheUI: i.asString == getUserData(applicationContext).UniqueId")
                    isHearted = true
                    break
                } else {
                    isHearted = false
                }
            }
            setHeartLottieAnimation()
        } else {
            val animator = ValueAnimator.ofFloat(0.1f, 0.5f).setDuration(500)
            animator.addUpdateListener { animation: ValueAnimator ->
                user_heart_lottieanimation_among_user_information.progress = animation.animatedValue as Float
            }
            animator.start()
        }

        user_practice_memory_score_text_view_among_user_information.text = practiceMemoryScore
        user_practice_memory_difference_text_view_among_user_information.text = practiceMemoryDifference
        user_practice_concentraction_score_text_view_among_user_information.text = practiceConcentractionScore
        user_practice_concentraction_difference_text_view_among_user_information.text = practiceConcentractionDifference
        user_practice_quickness_score_text_view_among_user_information.text = practiceQuicknessScore
        user_practice_quickness_difference_text_view_among_user_information.text = practiceQuicknessDifference

        user_test_memory_score_text_view_among_user_information.text = testMemoryScore
        user_test_memory_difference_text_view_among_user_information.text = testMemoryDifference
        user_test_concentraction_score_text_view_among_user_information.text = testConcentractionScore
        user_test_concentraction_difference_text_view_among_user_information.text = testConcentractionDifference
        user_test_quickness_score_text_view_among_user_information.text = testQuicknessScore
        user_test_quickness_difference_text_view_among_user_information.text = testQuicknessDifference

        user_test_level_text_view_among_user_information.text = level
        when(level) {
            "초보" -> user_test_level_image_view_among_user_information.setImageResource(R.drawable.bad_brain)
            "중수" -> user_test_level_image_view_among_user_information.setImageResource(R.drawable.normal_brain)
            "고수" -> user_test_level_image_view_among_user_information.setImageResource(R.drawable.good_brain)
            "천재" -> user_test_level_image_view_among_user_information.setImageResource(R.drawable.genius)
        }
    }

    private fun setHeartLottieAnimation() {
        if(!isHearted) {
            val animator = ValueAnimator.ofFloat(0.9f, 0.0f).setDuration(700)
            animator.addUpdateListener { animation: ValueAnimator ->
                user_heart_lottieanimation_among_user_information.progress = animation.animatedValue as Float
            }
            animator.start()
        } else if(isHearted){
            val animator = ValueAnimator.ofFloat(0.1f, 0.5f).setDuration(500)
            animator.addUpdateListener { animation: ValueAnimator ->
                user_heart_lottieanimation_among_user_information.progress = animation.animatedValue as Float
            }
            animator.start()
        }
    }

    private fun plusHeart() {
        postUserHeart.plusHeart(UserRoomMethod().getUserData(applicationContext).UniqueId, currentSeeUserUID).enqueue(object : Callback<HttpResponseCache> {
            override fun onFailure(call: Call<HttpResponseCache>, t: Throwable) {
                Log.d("TAG", "onResponse: plusHeart error is $t")
            }

            override fun onResponse(
                call: Call<HttpResponseCache>,
                response: Response<HttpResponseCache>
            ) {
                Log.d("TAG", "onResponse: plusHeart is successful")
            }

        })
    }

    private fun minusHeart() {
        postUserHeart.minusHeart(UserRoomMethod().getUserData(applicationContext).UniqueId, currentSeeUserUID).enqueue(object : Callback<HttpResponseCache> {
            override fun onFailure(call: Call<HttpResponseCache>, t: Throwable) {
                Log.d("TAG", "onFailure: minusHeart error is $t")
            }

            override fun onResponse(
                call: Call<HttpResponseCache>,
                response: Response<HttpResponseCache>
            ) {
                if(response.code() == 400) {
                    Toast.makeText(applicationContext, "에러", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("TAG", "onFailure: minusHeart is successful")
                }
            }

        })
    }


}