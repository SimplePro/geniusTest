package com.wotin.geniustest.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wotin.geniustest.CustomClass.SignInAndSignUpCustomClass
import com.wotin.geniustest.R
import com.wotin.geniustest.RetrofitSignInAndSignUp
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    lateinit var retrofit: Retrofit
    lateinit var apiService: RetrofitSignInAndSignUp
    lateinit var okHttpClient : OkHttpClient
    val baseUrl = "http://220.72.174.101:8080"

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
        apiService = retrofit.create(RetrofitSignInAndSignUp::class.java)

        login_button.setOnClickListener {
            if(login_id_edittext.text.isNotEmpty() && login_password_edittext.text.isNotEmpty())
            {
                apiService.signIn(id = login_id_edittext.text.toString(), password = login_password_edittext.text.toString()).enqueue(object : Callback<SignInAndSignUpCustomClass> {
                    override fun onFailure(call: Call<SignInAndSignUpCustomClass>, t: Throwable) {
                        Log.d("TAG", "error... $t")
                        Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<SignInAndSignUpCustomClass>,
                        response: Response<SignInAndSignUpCustomClass>
                    ) {
                        try{
                            if(response.body()!!.UniqueId == "") {
                                Toast.makeText(applicationContext, "존재하지 않는 사용자입니다.", Toast.LENGTH_LONG).show()
                            }
                            else {
                                Log.d("TAG", "response.body data is ${response.body()!!.id} ${response.body()!!.password} ${response.body()!!.UniqueId} ${response.body()!!.name}")
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        } catch (e : Exception) {
                            Log.d("TAG", "error is ${e.message}")
                            Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_LONG).show()
                        }
                    }

                })
            }
            else Toast.makeText(applicationContext, "id 와 password 를 입력해주세요", Toast.LENGTH_LONG).show()
        }

        go_to_signup_activity_button.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}