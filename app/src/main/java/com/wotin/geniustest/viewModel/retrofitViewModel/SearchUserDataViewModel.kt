package com.wotin.geniustest.viewModel.retrofitViewModel

import android.app.Application
import android.net.http.HttpResponseCache
import android.util.Log
import androidx.lifecycle.*
import com.wotin.geniustest.customClass.SearchUserCustomClass
import com.wotin.geniustest.repositories.SearchUserDataRepository
import com.wotin.geniustest.retrofitBuilder.GeniusRetrofitBuilder
import com.wotin.geniustest.roomMethod.UserRoomMethod
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchUserDataViewModel(application: Application) : AndroidViewModel(application) {
    var _userId : String = ""
    var modelData : MutableLiveData<SearchUserCustomClass> = MutableLiveData()

    init {
        _userId = UserRoomMethod().getUserData(application.applicationContext).id
        Log.d("TAG", "SearchUserDataViewModel init - userId")
    }

    fun setSearchId(id : String) {
        _userId = id
        Log.d("TAG", "_userId is $_userId")
        modelData = SearchUserDataRepository.getUserData(_userId, getApplication())
    }

    fun setIsHearted(isHearted : Boolean) {
        modelData.value!!.isHearted = isHearted
    }

    fun plusHeart() {
        modelData.value!!.heart += 1
        setIsHearted(true)
        val currentUniqueId = UserRoomMethod().getUserData(getApplication()).UniqueId
        val heartToUniqueId = modelData.value!!.currentSeeUserUID
        Log.d("TAG", "currentUniqueId is $currentUniqueId, heartToUniqueId is $heartToUniqueId")
        GeniusRetrofitBuilder.postUserHeartApiService.plusHeart(currentUniqueId, heartToUniqueId).enqueue(object :
            Callback<HttpResponseCache> {
            override fun onFailure(call: Call<HttpResponseCache>, t: Throwable) {
                Log.d("TAG", "plusHeart error is $t")
            }

            override fun onResponse(
                call: Call<HttpResponseCache>,
                response: Response<HttpResponseCache>
            ) {
                Log.d("TAG", "plusHeart response body is ${response.body()!!}")
            }
        })
    }

    fun minusHeart() {
        modelData.value!!.heart -= 1
        setIsHearted(false)
        val currentUniqueId = UserRoomMethod().getUserData(getApplication()).UniqueId
        val heartToUniqueId = modelData.value!!.currentSeeUserUID
        Log.d("TAG", "currentUniqueId is $currentUniqueId, heartToUniqueId is $heartToUniqueId")
        GeniusRetrofitBuilder.postUserHeartApiService.minusHeart(currentUniqueId, heartToUniqueId).enqueue(object :
            Callback<HttpResponseCache> {
            override fun onFailure(call: Call<HttpResponseCache>, t: Throwable) {
                Log.d("TAG", "minusHeart error is $t")
            }

            override fun onResponse(
                call: Call<HttpResponseCache>,
                response: Response<HttpResponseCache>
            ) {
                Log.d("TAG", "minusHeart response body is ${response.body()!!}")
            }
        })
    }

}