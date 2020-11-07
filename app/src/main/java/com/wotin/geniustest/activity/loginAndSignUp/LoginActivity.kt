package com.wotin.geniustest.activity.loginAndSignUp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonArray
import com.wotin.geniustest.*
import com.wotin.geniustest.activity.MainActivity
import com.wotin.geniustest.converters.MapJsonConverter
import com.wotin.geniustest.customClass.GeniusPractice.GeniusPracticeDataCustomClass
import com.wotin.geniustest.customClass.GeniusTest.GeniusTestDataCustomClass
import com.wotin.geniustest.customClass.RetrofitGetGeniusPracticeAndTestDataCustomClass
import com.wotin.geniustest.customClass.SignInAndSignUpCustomClass
import com.wotin.geniustest.roomMethod.InsertRoomMethod
import com.wotin.geniustest.roomMethod.UserRoomMethod
import kotlinx.android.synthetic.main.activity_login.*
import com.wotin.geniustest.retrofitBuilder.GeniusRetrofitBuilder.signInAndSignUpApiService
import com.wotin.geniustest.retrofitBuilder.GeniusRetrofitBuilder.getUserDataAndGeniusDataApiService
import com.wotin.geniustest.retrofitBuilder.GeniusRetrofitBuilder.forgotIdAndPasswordApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
                                    getUserDataAndGeniusDataApiService.getGeniusData(response.body()!!.UniqueId).enqueue(object : Callback<RetrofitGetGeniusPracticeAndTestDataCustomClass> {
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