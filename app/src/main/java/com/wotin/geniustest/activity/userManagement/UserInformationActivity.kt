package com.wotin.geniustest.activity.userManagement

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wotin.geniustest.EncryptionAndDetoxification
import com.wotin.geniustest.R
import com.wotin.geniustest.activity.MainActivity
import com.wotin.geniustest.customClass.SearchUserCustomClass
import com.wotin.geniustest.databinding.ActivityUserInformationBinding
import com.wotin.geniustest.roomMethod.UserRoomMethod
import com.wotin.geniustest.viewModel.retrofitViewModel.SearchUserDataViewModel
import java.lang.Exception
import kotlin.concurrent.timer


class UserInformationActivity : AppCompatActivity() {

    lateinit var mBinding : ActivityUserInformationBinding
    val searchViewModel : SearchUserDataViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this@UserInformationActivity, R.layout.activity_user_information)
        mBinding.activity = this@UserInformationActivity
        mBinding.searchViewModel = searchViewModel
        mBinding.lifecycleOwner = this

        if(intent.hasExtra("userId")) searchViewModel.setSearchId(intent.getStringExtra("userId")!!)

        mBinding.refreshLayoutAmongUserInformation.setOnRefreshListener {
            searchViewModel.setSearchId(searchViewModel._userId)
            mBinding.refreshLayoutAmongUserInformation.isRefreshing = false
        }

        mBinding.goToMainactivityFromUserInformationActivityImageview.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun onClickHeartButton() {
        Log.d("TAG", "clickedHeartButton")
        if(searchViewModel._userId == UserRoomMethod().getUserData(applicationContext).id) {
            Log.d("TAG", "try heart to me")
            Toast.makeText(applicationContext, "자기 자신에게는 하트를 할 수 없습니다.", Toast.LENGTH_SHORT).show()
        } else {
            if(searchViewModel.modelData.value!!.isHearted) {
                Log.d("TAG", "minusHeart")
                searchViewModel.minusHeart()
            }
            else if(!searchViewModel.modelData.value!!.isHearted){
                Log.d("TAG", "plusHeart")
                searchViewModel.plusHeart()
            }
            mBinding.searchViewModel = searchViewModel
        }
    }

    fun onClickSearchButton() {
        Log.d("TAG", "clickedSearchButton")
        if(mBinding.searchUserInformationEdittext.text.isNotEmpty()) {
            searchViewModel.setSearchId(EncryptionAndDetoxification().encryptionAndDetoxification(mBinding.searchUserInformationEdittext.text.toString()))
        } else {
            searchViewModel.setSearchId(UserRoomMethod().getUserData(applicationContext).id)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}