package com.wotin.geniustest.Activity.LoginAndSignUp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuView
import com.google.gson.JsonArray
import com.wotin.geniustest.*
import com.wotin.geniustest.Activity.MainActivity
import com.wotin.geniustest.Converters.MapJsonConverter
import com.wotin.geniustest.CustomClass.GeniusPractice.GeniusPracticeDataCustomClass
import com.wotin.geniustest.CustomClass.GeniusTest.GeniusTestDataCustomClass
import com.wotin.geniustest.CustomClass.RetrofitGetGeniusPracticeAndTestDataCustomClass
import com.wotin.geniustest.CustomClass.SignInAndSignUpCustomClass
import com.wotin.geniustest.RetrofitInterface.User.RetrofitSignInAndSignUp
import com.wotin.geniustest.RetrofitInterface.UserManagement.RetrofitForgotIdAndPassword
import com.wotin.geniustest.RetrofitInterface.UserManagement.RetrofitUserDataAndGeniusData
import com.wotin.geniustest.RoomMethod.InsertRoomMethod
import com.wotin.geniustest.RoomMethod.UserRoomMethod
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_user_information.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.microedition.khronos.egl.EGLDisplay

class LoginActivity : AppCompatActivity() {

    lateinit var retrofit: Retrofit
    lateinit var signInAndSignUpApiService: RetrofitSignInAndSignUp
    lateinit var getGeniusDataApiService : RetrofitUserDataAndGeniusData
    lateinit var forgotIdAndPasswordApiService : RetrofitForgotIdAndPassword
    lateinit var okHttpClient: OkHttpClient
    val baseUrl = "http://220.117.41.156:8080"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
        forgotIdAndPasswordApiService = retrofit.create(RetrofitForgotIdAndPassword::class.java)

        login_button.setOnClickListener {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if(networkState(connectivityManager)) {

                if (login_id_edittext.text!!.isNotEmpty() && login_password_edittext.text!!.isNotEmpty() && login_id_edittext!!.text!!.trim().length >= 7 && login_password_edittext!!.text!!.length >= 8) {
                    signInAndSignUpApiService.signIn(
                        id = EncryptionAndDetoxification()
                            .encryptionAndDetoxification(login_id_edittext.text.toString()),
                        password = EncryptionAndDetoxification()
                            .encryptionAndDetoxification(login_password_edittext.text.toString())
                    ).enqueue(object : Callback<SignInAndSignUpCustomClass> {
                        override fun onFailure(call: Call<SignInAndSignUpCustomClass>, t: Throwable) {
                            Log.d("TAG", "error... $t")
                            Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(
                            call: Call<SignInAndSignUpCustomClass>,
                            response: Response<SignInAndSignUpCustomClass>
                        ) {
                            try {
                                if (response.body()!!.UniqueId == "") {
                                    Toast.makeText(
                                        applicationContext,
                                        "존재하지 않는 사용자입니다.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Log.d(
                                        "TAG",
                                        "response.body data is ${response.body()!!.id} ${response.body()!!.password} ${response.body()!!.UniqueId} ${response.body()!!.name}"
                                    )
                                    Log.d(
                                        "TAG", "response.body data is " +
                                                EncryptionAndDetoxification()
                                                    .encryptionAndDetoxification(response.body()!!.id) + " " +
                                                EncryptionAndDetoxification()
                                                    .encryptionAndDetoxification(response.body()!!.password) + " " +
                                                response.body()!!.UniqueId + " " + response.body()!!.name
                                    )
                                    UserRoomMethod().insertUserData(
                                        name = response.body()!!.name,
                                        id = response.body()!!.id,
                                        password = response.body()!!.password,
                                        UniqueId = response.body()!!.UniqueId,
                                        context = applicationContext
                                    )
                                    saveUserDataSharedPreference(response.body()!!.UniqueId, response.body()!!.id, response.body()!!.password)
                                    getGeniusDataApiService.getGeniusData(response.body()!!.UniqueId).enqueue(object : Callback<RetrofitGetGeniusPracticeAndTestDataCustomClass> {
                                        override fun onFailure(call: Call<RetrofitGetGeniusPracticeAndTestDataCustomClass>, t: Throwable) {
                                            Toast.makeText(applicationContext, "데이터를 가져오는데 실패하였습니다", Toast.LENGTH_LONG).show()
                                            Log.d("TAG", "getGeniusDataApiService error is $t")
                                        }

                                        override fun onResponse(
                                            call: Call<RetrofitGetGeniusPracticeAndTestDataCustomClass>,
                                            response: Response<RetrofitGetGeniusPracticeAndTestDataCustomClass>
                                        ) {
                                            val uniqueId = response.body()!!.UniqueId
                                            val testLevel = response.body()!!.level
                                            val bestScore = MapJsonConverter().MapToJsonConverter(response.body()!!.best_score.toString())
                                            val practiceJson = MapJsonConverter().MapToJsonConverter(bestScore["practice"].toString())
                                            val testJson = MapJsonConverter().MapToJsonConverter(bestScore["test"].toString())



                                            val practice : GeniusPracticeDataCustomClass = GeniusPracticeDataCustomClass(UniqueId = uniqueId,
                                                concentractionScore = practiceJson["practice_concentraction_score"].toString().toFloat().toInt().toString(),
                                                memoryScore = practiceJson["practice_memory_score"].toString().toFloat().toInt().toString(),
                                                concentractionDifference = practiceJson["practice_concentraction_difference"].toString(),
                                                memoryDifference = practiceJson["practice_memory_difference"].toString(),
                                                quicknessScore = practiceJson["practice_quickness_score"].toString(),
                                                quicknessDifference = practiceJson["practice_quickness_difference"].toString())

                                            val test : GeniusTestDataCustomClass = GeniusTestDataCustomClass(UniqueId = uniqueId,
                                                concentractionScore = testJson["test_concentraction_score"].toString().toFloat().toInt().toString(),
                                                memoryScore = testJson["test_memory_score"].toString().toFloat().toInt().toString(),
                                                concentractionDifference = testJson["test_concentraction_difference"].toString(),
                                                memoryDifference = testJson["test_memory_difference"].toString(),
                                                quicknessScore = testJson["test_quickness_score"].toString(),
                                                quicknessDifference = testJson["test_quickness_difference"].toString(),
                                                level = testLevel)
                                            Log.d("TAG",
                                                "getGeniusDataApiService bestScore is $bestScore")
                                            Log.d(
                                                "TAG",
                                                "getGeniusDataApiService practice is $practice, test is $test"
                                            )
                                            InsertRoomMethod().insertGeniusPracticeData(practice, applicationContext)
                                            InsertRoomMethod().insertGeniusTestData(test, applicationContext)
                                            InsertRoomMethod().insertTestModeData(applicationContext)
                                            val intent =
                                                Intent(this@LoginActivity, MainActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                    })
                                }
                            } catch (e: Exception) {
                                Log.d("TAG", "error is ${e.message}")
                                Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_LONG).show()
                            }
                        }

                    })
                } else if(login_id_edittext!!.text!!.isEmpty() || login_password_edittext.text!!.isEmpty()) Toast.makeText(applicationContext, "id 와 password 를 입력해주세요", Toast.LENGTH_LONG).show()
                else if(login_id_edittext!!.text!!.trim().length < 7 || login_password_edittext.text!!.length < 8) Toast.makeText(applicationContext, "id 와 password 를 각각 7, 8 자 이상 입력해주세요", Toast.LENGTH_LONG).show()
            } else Toast.makeText(applicationContext, "네트워크 연결상태가 좋지 못합니다.", Toast.LENGTH_LONG).show()
        }

        go_to_signup_activity_button.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        forgot_id_textview.setOnClickListener {
            showForgotIdDialog()
        }

    }

    private fun saveUserDataSharedPreference(UID : String, id : String, password : String) {
        val pref = getPreferences(0)
        val editor = pref.edit()

        editor.putString("UID", UID)
            .putString("id", id)
            .putString("password", password)
            .apply()
    }

    private fun showForgotIdDialog() {
        val dialog = AlertDialog.Builder(this@LoginActivity)
        val EDialog = LayoutInflater.from(this@LoginActivity)
        val MView = EDialog.inflate(R.layout.forgot_id_dialog, null)
        val builder = dialog.create()
        val listData = arrayListOf<String>()
        val forgotListView : ListView = MView.findViewById(R.id.forgot_id_listview)
        val forgotEditText : EditText = MView.findViewById(R.id.forgot_id_edittext)
        val forgotButton : Button = MView.findViewById(R.id.forgot_id_button)
        forgotButton.setOnClickListener {
            Log.d("TAG", "getIdForName forgotButton is clicked")
            forgotIdAndPasswordApiService.getIdForName(name = forgotEditText.text.toString()).enqueue(object : Callback<JsonArray> {
                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    Toast.makeText(applicationContext, "데이터를 불러오는데 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "getIdForName error is $t")
                }

                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    for(jsonElement in response.body()!!) {
                        listData.add(EncryptionAndDetoxification().encryptionAndDetoxification(jsonElement.asString))
                    }
                    Log.d("TAG", "getIdForName listData is $listData")
                    val adapter = ArrayAdapter(this@LoginActivity, android.R.layout.simple_list_item_1, listData)
                    forgotListView.adapter = adapter
                }

            })
        }

        builder.setView(MView)
        builder.show()
    }

}