package com.wotin.geniustest.Repositories.RetrofitRepositories.User

import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.wotin.geniustest.Repositories.RetrofitRepositories.Genius.GeniusDataRepository
import com.wotin.geniustest.RetrofitBuilder.GeniusRetrofitBuilder
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SearchUserDataRepository {
    var job : CompletableJob? = null

    fun getUserData(id : String): LiveData<JsonObject?> {
        job = Job()
        return object : LiveData<JsonObject?>() {
            override fun onActive() {
                super.onActive()
                GeniusDataRepository.job?.let { theJob ->
                    CoroutineScope(Dispatchers.IO + theJob).launch {
                        var data : JsonObject? = null
                        GeniusRetrofitBuilder.searchUserDataApiService.getUserData(id).enqueue(object :
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