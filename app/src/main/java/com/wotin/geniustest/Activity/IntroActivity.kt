package com.wotin.geniustest.Activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.gson.JsonObject
import com.wotin.geniustest.*
import com.wotin.geniustest.Activity.LoginAndSignUp.LoginActivity
import com.wotin.geniustest.Converters.MapJsonConverter
import com.wotin.geniustest.CustomClass.GeniusPractice.GeniusPracticeDataCustomClass
import com.wotin.geniustest.CustomClass.GeniusTest.GeniusTestDataCustomClass
import com.wotin.geniustest.CustomClass.RetrofitGetGeniusPracticeAndTestDataCustomClass
import com.wotin.geniustest.CustomClass.SignInAndSignUpCustomClass
import com.wotin.geniustest.CustomClass.UserCustomClass
import com.wotin.geniustest.DB.UserDB
import com.wotin.geniustest.RetrofitInterface.RetrofitServerCheck
import com.wotin.geniustest.RetrofitInterface.User.RetrofitSignInAndSignUp
import com.wotin.geniustest.RetrofitInterface.RetrofitUserDataAndGeniusData
import com.wotin.geniustest.RoomMethod.DeleteRoomMethod
import com.wotin.geniustest.RoomMethod.InsertRoomMethod
import com.wotin.geniustest.RoomMethod.UserRoomMethod
import kotlinx.android.synthetic.main.activity_intro.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit

class IntroActivity : AppCompatActivity() {
    var handler: Handler? = null
    var runnable: Runnable? = null

    lateinit var retrofit: Retrofit
    lateinit var signInAndSignUpApiService: RetrofitSignInAndSignUp
    lateinit var getGeniusDataApiService : RetrofitUserDataAndGeniusData
    lateinit var okHttpClient: OkHttpClient
    val baseUrl = "http://220.72.174.101:8080"

    var UID : String? = ""
    var id : String?  = ""
    var password : String? = ""

    var serverCheckFromTime = ""
    var serverCheckToTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        handler = Handler()

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

        signInAndSignUpApiService = retrofit.create(RetrofitSignInAndSignUp::class.java)
        getGeniusDataApiService = retrofit.create(RetrofitUserDataAndGeniusData::class.java)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

    }

    private fun getUserData() : UserCustomClass {
        val userDB : UserDB = Room.databaseBuilder(
            applicationContext,
            UserDB::class.java, "user.db"
        ).allowMainThreadQueries()
            .build()
        val userData : UserCustomClass = userDB.userDB().getAll()
        return userData
    }


    private suspend fun serverCheck() : Boolean? {
        val geniusTestServerCheck = retrofit.create(RetrofitServerCheck::class.java)

        var returnValue : Boolean? = null

        geniusTestServerCheck.serverCheck().enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                returnValue = null
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("TAG", "geniusTestServerCheck onResponse ${response.body()!!}")
                if(response.body()!!.get("from_time").asString.isNotEmpty()) {
                    serverCheckFromTime = response.body()!!["from_time"].asString
                    serverCheckToTime = response.body()!!["to_time"].asString
                    Log.d("TAG", "onResponse: serverCheckFromTime is $serverCheckFromTime serverCheckToTime is $serverCheckToTime")
                    server_check_from_time_textview.text = serverCheckFromTime
                    server_check_to_time_textview.text = serverCheckToTime
                    server_check_layout.visibility = View.VISIBLE
                    returnValue = true
                    handler?.removeCallbacks(runnable)
                } else returnValue = false
            }
        })
        delay(500L)
        return returnValue
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch {
            setNextIntent()
        }
    }

    suspend fun setNextIntent() {
        val serverCheckVariable = serverCheck()
        delay(500L)
        when (serverCheckVariable) {
            null -> {
                runOnUiThread {
                    Toast.makeText(applicationContext, "에러\n잠시후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    handler?.removeCallbacks(runnable)
                    Log.d("TAG", "onResume: serverCheck is null")
                }
        }
            false -> {
                runOnUiThread {
                    Log.d("TAG", "onResume: serverCheck is false")
                    runnable = Runnable {
                        try {
                            val userData = getUserData()
                            val intent = if (userData == null) {
                                Intent(this, LoginActivity::class.java)
                            } else {
                                Intent(this, MainActivity::class.java)
                            }
                            startActivity(intent)
                            finish()
                        } catch (e: Exception) {
                            DeleteRoomMethod().deleteUserDataAndGeniusTestAndPracticeData(
                                applicationContext
                            )
                            val connectivityManager =
                                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                            if (networkState(connectivityManager)) {
                                getUserDataSharedPreference()
                                if (id!!.isEmpty() || password!!.isEmpty()) {
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    signInAndSignUpApiService.signIn(id = id!!, password = password!!)
                                        .enqueue(object :
                                            retrofit2.Callback<SignInAndSignUpCustomClass> {
                                            override fun onFailure(
                                                call: Call<SignInAndSignUpCustomClass>,
                                                t: Throwable
                                            ) {
                                                val intent = Intent(
                                                    this@IntroActivity,
                                                    LoginActivity::class.java
                                                )
                                                startActivity(intent)
                                                finish()
                                            }

                                            override fun onResponse(
                                                call: Call<SignInAndSignUpCustomClass>,
                                                response: Response<SignInAndSignUpCustomClass>
                                            ) {
                                                UserRoomMethod().insertUserData(
                                                    UniqueId = response.body()!!.UniqueId,
                                                    id = response.body()!!.id,
                                                    password = response.body()!!.password,
                                                    name = response.body()!!.name,
                                                    context = applicationContext
                                                )

                                                getGeniusDataApiService.getGeniusData(UID!!)
                                                    .enqueue(object :
                                                        retrofit2.Callback<RetrofitGetGeniusPracticeAndTestDataCustomClass> {
                                                        override fun onFailure(
                                                            call: Call<RetrofitGetGeniusPracticeAndTestDataCustomClass>,
                                                            t: Throwable
                                                        ) {
                                                            val intent = Intent(
                                                                this@IntroActivity,
                                                                LoginActivity::class.java
                                                            )
                                                            startActivity(intent)
                                                            finish()
                                                        }

                                                        override fun onResponse(
                                                            call: Call<RetrofitGetGeniusPracticeAndTestDataCustomClass>,
                                                            response: Response<RetrofitGetGeniusPracticeAndTestDataCustomClass>
                                                        ) {
                                                            val uniqueId = response.body()!!.UniqueId
                                                            val testLevel = response.body()!!.level
                                                            val bestScore =
                                                                MapJsonConverter().MapToJsonConverter(
                                                                    response.body()!!.best_score.toString()
                                                                )
                                                            val practiceJson =
                                                                MapJsonConverter().MapToJsonConverter(
                                                                    bestScore["practice"].toString()
                                                                )
                                                            val testJson =
                                                                MapJsonConverter().MapToJsonConverter(
                                                                    bestScore["test"].toString()
                                                                )

                                                            val practice: GeniusPracticeDataCustomClass =
                                                                GeniusPracticeDataCustomClass(
                                                                    UniqueId = uniqueId,
                                                                    concentractionScore = practiceJson["practice_concentraction_score"].toString()
                                                                        .toFloat().toInt().toString(),
                                                                    memoryScore = practiceJson["practice_memory_score"].toString()
                                                                        .toFloat().toInt().toString(),
                                                                    concentractionDifference = practiceJson["practice_concentraction_difference"].toString(),
                                                                    memoryDifference = practiceJson["practice_memory_difference"].toString(),
                                                                    quicknessScore = practiceJson["practice_quickness_score"].toString(),
                                                                    quicknessDifference = practiceJson["practice_quickness_difference"].toString()
                                                                )

                                                            val test: GeniusTestDataCustomClass =
                                                                GeniusTestDataCustomClass(
                                                                    UniqueId = uniqueId,
                                                                    concentractionScore = testJson["test_concentraction_score"].toString()
                                                                        .toFloat().toInt().toString(),
                                                                    memoryScore = testJson["test_memory_score"].toString()
                                                                        .toFloat().toInt().toString(),
                                                                    concentractionDifference = testJson["test_concentraction_difference"].toString(),
                                                                    memoryDifference = testJson["test_memory_difference"].toString(),
                                                                    quicknessScore = testJson["test_quickness_score"].toString(),
                                                                    quicknessDifference = testJson["test_quickness_difference"].toString(),
                                                                    level = testLevel
                                                                )

                                                            InsertRoomMethod().insertGeniusPracticeData(
                                                                practice,
                                                                applicationContext
                                                            )
                                                            InsertRoomMethod().insertGeniusTestData(
                                                                test,
                                                                applicationContext
                                                            )

                                                            val intent = Intent(
                                                                this@IntroActivity,
                                                                MainActivity::class.java
                                                            )
                                                            startActivity(intent)
                                                            finish()
                                                        }

                                                    })

                                            }

                                        })
                                }
                            } else {
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    handler?.run {
                        postDelayed(runnable, 3000)
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        handler?.removeCallbacks(runnable)
    }

    private fun getUserDataSharedPreference() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        UID = sharedPref.getString("UID", "")
        id = sharedPref.getString("id", "")
        password = sharedPref.getString("password", "")
    }

}