package com.wotin.geniustest.Activity.LoginAndSignUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.wotin.geniustest.CustomClass.SignInAndSignUpCustomClass
import com.wotin.geniustest.EncryptionAndDetoxification.EncryptionAndDetoxification
import com.wotin.geniustest.R
import com.wotin.geniustest.RetrofitInterface.RetrofitSignInAndSignUp
import kotlinx.android.synthetic.main.activity_sign_up.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit

class SignUpActivity : AppCompatActivity() {


    lateinit var retrofit: Retrofit
    lateinit var apiService: RetrofitSignInAndSignUp
    lateinit var okHttpClient : OkHttpClient
    val baseUrl = "http://220.72.174.101:8080"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

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
        apiService = retrofit.create(RetrofitSignInAndSignUp::class.java)

        signup_button.setOnClickListener {
            if(signup_name_edittext.text.isNotEmpty() && signup_id_edittext.text.isNotEmpty() && signup_password_edittext.text.isNotEmpty()) {
                val UniqueId = UUID.randomUUID().toString().replace("-", "")
                Log.d("TAG", "UniqueId is $UniqueId")
                apiService.signUp(
                    name = signup_name_edittext.text.toString(),
                    id = EncryptionAndDetoxification()
                        .encryptionAndDetoxification(signup_id_edittext.text.toString()),
                    password = EncryptionAndDetoxification()
                        .encryptionAndDetoxification(signup_password_edittext.text.toString()),
                    UniqueId = UniqueId
                )
                    .enqueue(object : Callback<SignInAndSignUpCustomClass> {
                        override fun onFailure(
                            call: Call<SignInAndSignUpCustomClass>,
                            t: Throwable
                        ) {
                            Log.d("TAG", "회원가입 에러 $t")
                            Toast.makeText(applicationContext, "회원가입 실패", Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(
                            call: Call<SignInAndSignUpCustomClass>,
                            response: Response<SignInAndSignUpCustomClass>
                        ) {
                            try {
                                Log.d("TAG", "response.body()!! data is ${response.body()!!.id} ${response.body()!!.password} ${response.body()!!.UniqueId} ${response.body()!!.name}")
                                if(response.body()!!.UniqueId.isNotEmpty()) {
                                    val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else Toast.makeText(applicationContext, "아이디가 중복되었습니다.", Toast.LENGTH_LONG).show()
                            } catch (e : Exception) {
                                Toast.makeText(applicationContext, "회원가입 실패", Toast.LENGTH_LONG).show()
                                Log.d("TAG", "회원가입 에러 ${e.message}")
                            }
                        }

                    })
            } else Toast.makeText(applicationContext, "name, id, password 를 입력해주세요.", Toast.LENGTH_LONG).show()
        }

        go_to_login_activity_button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}