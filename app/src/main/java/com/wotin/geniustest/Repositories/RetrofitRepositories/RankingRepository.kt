package com.wotin.geniustest.Repositories.RetrofitRepositories

import androidx.lifecycle.LiveData
import com.google.gson.JsonArray
import com.wotin.geniustest.Repositories.RetrofitRepositories.Genius.GeniusDataRepository
import com.wotin.geniustest.RetrofitBuilder.GeniusRetrofitBuilder
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RankingRepository {
    var job : CompletableJob? = null

    fun getGeniusTestRankData(): LiveData<JsonArray?> {
        job = Job()
        return object : LiveData<JsonArray?>() {
            override fun onActive() {
                super.onActive()
                GeniusDataRepository.job?.let { theJob ->
                    CoroutineScope(Dispatchers.IO + theJob).launch {
                        var data : JsonArray? = null
                        GeniusRetrofitBuilder.getGeniusTestRankDataApiService.getGeniusTestRankData().enqueue(object :
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