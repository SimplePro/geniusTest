package com.wotin.geniustest.Repositories.RetrofitRepositories.UserManagement

import androidx.lifecycle.LiveData
import com.wotin.geniustest.CustomClass.RetrofitGetGeniusPracticeAndTestDataCustomClass
import com.wotin.geniustest.Repositories.RetrofitRepositories.Genius.GeniusDataRepository
import com.wotin.geniustest.RetrofitBuilder.GeniusRetrofitBuilder
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserDataAndGeniusDataRepository {
    var job : CompletableJob? = null

    fun getGeniusData(pk : String): LiveData<RetrofitGetGeniusPracticeAndTestDataCustomClass?> {
        job = Job()
        return object : LiveData<RetrofitGetGeniusPracticeAndTestDataCustomClass?>() {
            override fun onActive() {
                super.onActive()
                GeniusDataRepository.job?.let { theJob ->
                    CoroutineScope(Dispatchers.IO + theJob).launch {
                        var data : RetrofitGetGeniusPracticeAndTestDataCustomClass? = null
                        GeniusRetrofitBuilder.getUserDataAndGeniusDataApiService.getGeniusData(pk).enqueue(object :
                            Callback<RetrofitGetGeniusPracticeAndTestDataCustomClass> {
                            override fun onFailure(call: Call<RetrofitGetGeniusPracticeAndTestDataCustomClass>, t: Throwable) {
                                data = null
                            }

                            override fun onResponse(
                                call: Call<RetrofitGetGeniusPracticeAndTestDataCustomClass>,
                                response: Response<RetrofitGetGeniusPracticeAndTestDataCustomClass>
                            ) {
                                data = response.body()
                            }

                        })
                        withContext(Dispatchers.Main) {
                            value = data
                            theJob.complete()
                        }
                    }
                }
            }
        }
    }

    fun cancelJobs() {
        job?.cancel()
    }
}