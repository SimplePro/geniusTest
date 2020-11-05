package com.wotin.geniustest.Repositories.RetrofitRepositories

import androidx.lifecycle.LiveData
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.wotin.geniustest.RetrofitBuilder.GeniusRetrofitBuilder
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object HeartRepository {
    var job : CompletableJob? = null

    fun getHeartToMePeople(pk : String): LiveData<JsonArray?> {
        job = Job()
        return object : LiveData<JsonArray?>() {
            override fun onActive() {
                super.onActive()
                GeniusDataRepository.job?.let { theJob ->
                    CoroutineScope(Dispatchers.IO + theJob).launch {
                        var data : JsonArray? = null
                        GeniusRetrofitBuilder.getAboutHeartApiService.getHeartToMePeople(pk).enqueue(object :
                            Callback<JsonArray> {
                            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                                data = null
                            }

                            override fun onResponse(
                                call: Call<JsonArray>,
                                response: Response<JsonArray>
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

    fun getHeartToPeople(pk : String): LiveData<JsonArray?> {
        job = Job()
        return object : LiveData<JsonArray?>() {
            override fun onActive() {
                super.onActive()
                GeniusDataRepository.job?.let { theJob ->
                    CoroutineScope(Dispatchers.IO + theJob).launch {
                        var data : JsonArray? = null
                        GeniusRetrofitBuilder.getAboutHeartApiService.getHeartToPeople(pk).enqueue(object :
                            Callback<JsonArray> {
                            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                                data = null
                            }

                            override fun onResponse(
                                call: Call<JsonArray>,
                                response: Response<JsonArray>
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