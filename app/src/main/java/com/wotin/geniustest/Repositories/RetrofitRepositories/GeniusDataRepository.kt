package com.wotin.geniustest.Repositories.RetrofitRepositories

import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.wotin.geniustest.RetrofitBuilder.GeniusRetrofitBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.Dispatcher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object GeniusDataRepository {
    var job : CompletableJob? = null

    fun getGeniusPracticeMemoryDifference(memory_score : String, pk : String): LiveData<JsonObject?> {
        job = Job()
        return object : LiveData<JsonObject?>() {
            override fun onActive() {
                super.onActive()
                job?.let { theJob ->
                    CoroutineScope(IO + theJob).launch {
                        var data : JsonObject? = null
                        GeniusRetrofitBuilder.geniusDataDifferenceApiService.getGeniusPracticeMemoryDifference(memory_score, pk).enqueue(object : Callback<JsonObject> {
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
                        withContext(Main) {
                            value = data
                            theJob.complete()
                        }

                    }
                }
            }
        }
    }

    fun getGeniusPracticeConcentractionDifference(concentraction_score : String, pk : String): LiveData<JsonObject?> {
        job = Job()
        return object : LiveData<JsonObject?>() {
            override fun onActive() {
                super.onActive()
                job?.let { theJob ->
                    CoroutineScope(IO + theJob).launch {
                        var data : JsonObject? = null
                        GeniusRetrofitBuilder.geniusDataDifferenceApiService.getGeniusPracticeConcentractionDifference(concentraction_score, pk).enqueue(object : Callback<JsonObject> {
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
                        withContext(Main) {
                            value = data
                            theJob.complete()
                        }

                    }
                }
            }
        }
    }

    fun getGeniusPracticeQuicknessDifference(quickness_score : String, pk : String): LiveData<JsonObject?> {
        job = Job()
        return object : LiveData<JsonObject?>() {
            override fun onActive() {
                super.onActive()
                job?.let { theJob ->
                    CoroutineScope(IO + theJob).launch {
                        var data : JsonObject? = null
                        GeniusRetrofitBuilder.geniusDataDifferenceApiService.getGeniusPracticeQuicknessDifference(quickness_score, pk).enqueue(object : Callback<JsonObject> {
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
                        withContext(Main) {
                            value = data
                            theJob.complete()
                        }

                    }
                }
            }
        }
    }


    fun getGeniusTestMemoryDifference(memory_score : String, pk : String): LiveData<JsonObject?> {
        job = Job()
        return object : LiveData<JsonObject?>() {
            override fun onActive() {
                super.onActive()
                job?.let { theJob ->
                    CoroutineScope(IO + theJob).launch {
                        var data : JsonObject? = null
                        GeniusRetrofitBuilder.geniusDataDifferenceApiService.getGeniusTestMemoryDifference(memory_score, pk).enqueue(object : Callback<JsonObject> {
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
                        withContext(Main) {
                            value = data
                            theJob.complete()
                        }

                    }
                }
            }
        }
    }

    fun getGeniusTestConcentractionDifference(concentraction_score : String, pk : String): LiveData<JsonObject?> {
        job = Job()
        return object : LiveData<JsonObject?>() {
            override fun onActive() {
                super.onActive()
                job?.let { theJob ->
                    CoroutineScope(IO + theJob).launch {
                        var data : JsonObject? = null
                        GeniusRetrofitBuilder.geniusDataDifferenceApiService.getGeniusTestConcentractionDifference(concentraction_score, pk).enqueue(object : Callback<JsonObject> {
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
                        withContext(Main) {
                            value = data
                            theJob.complete()
                        }

                    }
                }
            }
        }
    }

    fun getGeniusTestQuicknessDifference(quickness_score : String, pk : String): LiveData<JsonObject?> {
        job = Job()
        return object : LiveData<JsonObject?>() {
            override fun onActive() {
                super.onActive()
                job?.let { theJob ->
                    CoroutineScope(IO + theJob).launch {
                        var data : JsonObject? = null
                        GeniusRetrofitBuilder.geniusDataDifferenceApiService.getGeniusTestQuicknessDifference(quickness_score, pk).enqueue(object : Callback<JsonObject> {
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
                        withContext(Main) {
                            value = data
                            theJob.complete()
                        }

                    }
                }
            }
        }
    }


    fun getGeniusTestSumDifference(pk : String): LiveData<JsonObject?> {
        job = Job()
        return object : LiveData<JsonObject?>() {
            override fun onActive() {
                super.onActive()
                job?.let { theJob ->
                    CoroutineScope(IO + theJob).launch {
                        var data : JsonObject? = null
                        GeniusRetrofitBuilder.geniusDataDifferenceApiService.getGeniusTestSumDifference(pk).enqueue(object : Callback<JsonObject> {
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
                        withContext(Main) {
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