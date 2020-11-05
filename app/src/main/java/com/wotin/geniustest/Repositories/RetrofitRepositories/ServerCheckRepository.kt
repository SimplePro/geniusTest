package com.wotin.geniustest.Repositories.RetrofitRepositories

import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.wotin.geniustest.RetrofitBuilder.GeniusRetrofitBuilder
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ServerCheckRepository {
    var job : CompletableJob? = null

    fun serverCheck(): LiveData<JsonObject?> {
        job = Job()
        return object : LiveData<JsonObject?>() {
            override fun onActive() {
                super.onActive()
                GeniusDataRepository.job?.let { theJob ->
                    CoroutineScope(Dispatchers.IO + theJob).launch {
                        var data : JsonObject? = null
                        GeniusRetrofitBuilder.geniusTestServerCheckApiService.serverCheck().enqueue(object :
                            Callback<JsonObject> {
                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                data = null
                            }

                            override fun onResponse(
                                call: Call<JsonObject>,
                                response: Response<JsonObject>
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