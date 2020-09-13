package com.wotin.geniustest.Activity.UserManagement

import android.content.Intent
import android.net.http.HttpResponseCache
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.room.Room
import com.wotin.geniustest.Activity.LoginAndSignUp.LoginActivity
import com.wotin.geniustest.Activity.MainActivity
import com.wotin.geniustest.DB.UserDB
import com.wotin.geniustest.EncryptionAndDetoxification
import com.wotin.geniustest.R
import com.wotin.geniustest.RetrofitInterface.RetrofitDeleteAccountAndData
import com.wotin.geniustest.RetrofitInterface.RetrofitSignInAndSignUp
import com.wotin.geniustest.deleteUserDataAndGeniusTestAndPracticeData
import kotlinx.android.synthetic.main.activity_delete_user.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class DeleteUserActivity : AppCompatActivity() {

    lateinit var retrofit: Retrofit
    lateinit var apiService: RetrofitDeleteAccountAndData
    lateinit var okHttpClient : OkHttpClient
    val baseUrl = "http://220.72.174.101:8080"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_user)

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
        apiService = retrofit.create(RetrofitDeleteAccountAndData::class.java)

        val userDB : UserDB = Room.databaseBuilder(
            applicationContext,
            UserDB::class.java, "user.db"
        ).allowMainThreadQueries()
            .build()
        val userData = userDB.userDB().getAll()

        go_to_mainactivity_from_delete_account_imageview.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        delete_account_confirm_button.setOnClickListener {
            if(check_password_before_delete_account.text.toString() == EncryptionAndDetoxification().encryptionAndDetoxification(userData.password)) {
                apiService.deleteAccountAndData(userData.UniqueId).enqueue(object : Callback<HttpResponseCache> {
                    override fun onFailure(call: Call<HttpResponseCache>, t: Throwable) {
                        Toast.makeText(applicationContext, "계정이 삭제되지 않았습니다.", Toast.LENGTH_LONG).show()
                    }
                    override fun onResponse(
                        call: Call<HttpResponseCache>,
                        response: Response<HttpResponseCache>
                    ) {
                        if(response.code() == 204) {
                            Toast.makeText(applicationContext, "성공적으로 계정이 삭제되었습니다", Toast.LENGTH_LONG).show()
                            deleteUserDataAndGeniusTestAndPracticeData(applicationContext)
                            Toast.makeText(applicationContext, "${userData.name} 님 그동안 '천재 테스트' 를 즐겨주셔서 감사합니다.", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@DeleteUserActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(applicationContext, "계정이 삭제되지 않았습니다.", Toast.LENGTH_LONG).show()
                        }
                    }
                })
            } else {
                Toast.makeText(applicationContext, "비밀번호가 맞지 않습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}