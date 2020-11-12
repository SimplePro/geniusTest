package com.wotin.geniustest.activity.loginAndSignUp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.wotin.geniustest.customClass.SignInAndSignUpCustomClass
import com.wotin.geniustest.EncryptionAndDetoxification
import com.wotin.geniustest.R
import com.wotin.geniustest.databinding.ActivitySignUpBinding
import com.wotin.geniustest.retrofitBuilder.GeniusRetrofitBuilder
import com.wotin.geniustest.networkState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*

class SignUpActivity : AppCompatActivity() {
    var agreeTerms = false
    lateinit var mBinding : ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        mBinding.activity = this

        mBinding.showTermsDialogTextview.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            val EDialog = LayoutInflater.from(this)
            val MView = EDialog.inflate(R.layout.terms_dialog, null)
            val termsCheckBox = MView.findViewById<CheckBox>(R.id.terms_checkbox)
            val builder = dialog.create()
            builder.setView(MView)
            builder.show()
            termsCheckBox.isChecked = agreeTerms
            termsCheckBox.setOnClickListener { agreeTerms = termsCheckBox.isChecked }
        }

        mBinding.goToLoginActivityButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun clickedSignUpButton() {
        if (agreeTerms) {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if(networkState(connectivityManager)) {
                if(mBinding.signupNameEdittext.text!!.isNotEmpty() && mBinding.signupIdEdittext.text!!.isNotEmpty() && mBinding.signupPasswordEdittext.text!!.isNotEmpty()
                    && mBinding.signupIdEdittext.text!!.length >= 7 && mBinding.signupPasswordEdittext.text!!.length >= 8) {
                    val UniqueId = UUID.randomUUID().toString().replace("-", "")
                    Log.d("TAG", "UniqueId is $UniqueId")
                    GeniusRetrofitBuilder.signInAndSignUpApiService.signUp(
                        name = mBinding.signupNameEdittext.text.toString(),
                        id = EncryptionAndDetoxification().encryptionAndDetoxification(mBinding.signupIdEdittext.text.toString()),
                        password = EncryptionAndDetoxification().encryptionAndDetoxification(mBinding.signupPasswordEdittext.text.toString()),
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
                } else if(mBinding.signupIdEdittext.text!!.isEmpty() || mBinding.signupPasswordEdittext.text!!.isEmpty())
                    Toast.makeText(applicationContext, "user 과 id, password 를 입력해주세요", Toast.LENGTH_LONG).show()
                else if(mBinding.signupIdEdittext.text!!.trim().length < 7 || mBinding.signupPasswordEdittext.text!!.length < 8)
                    Toast.makeText(applicationContext, "id 와 password 를 각각 7, 8 자 이상 입력해주세요", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext, "네트워크 연결상태가 좋지 못합니다.", Toast.LENGTH_LONG).show()
            }
        } else Toast.makeText(applicationContext, "이용약관에 동의해주세요", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}