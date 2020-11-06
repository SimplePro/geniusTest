package com.wotin.geniustest.Repositories.RetrofitRepositories.Genius

import android.app.Application
import android.app.admin.DevicePolicyManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.wotin.geniustest.RetrofitBuilder.GeniusRetrofitBuilder
import com.wotin.geniustest.RoomMethod.GetRoomMethod
import com.wotin.geniustest.RoomMethod.UpdateRoomMethod
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

object GeniusDataRepository {
    var job : CompletableJob? = null

    fun getGeniusPracticeMemoryDifference(memory_score : String, pk : String, application : Application): LiveData<JsonObject?> {
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
                                val geniusPracticeData = GetRoomMethod().getGeniusPracticeData(application.applicationContext)
                                try {
                                    Log.d("TAG", "postDataToServer PracticeDifference data is ${response.body()}")
                                    val practiceMemoryDifference = response.body()!!.get("practice_memory_difference")
                                    if(practiceMemoryDifference.asString.isNotEmpty()) {
                                        geniusPracticeData.memoryScore = memory_score
                                        geniusPracticeData.memoryDifference = practiceMemoryDifference.asString
                                        UpdateRoomMethod().updateGeniusPracticeData(context = application.applicationContext, geniusPracticeData = geniusPracticeData)
                                    }
                                } catch (e : Exception) {
                                    Toast.makeText(application.applicationContext, "에러", Toast.LENGTH_LONG).show()
                                    Log.d("TAG", "postDataToServer PracticeDifference error is ${e.message}")
                                }
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

    fun getGeniusPracticeConcentractionDifference(concentraction_score : String, pk : String, application: Application): LiveData<JsonObject?> {
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
                                val geniusPracticeData = GetRoomMethod().getGeniusPracticeData(application.applicationContext)
                                try {
                                    Log.d("TAG", "postDataToServer PracticeDifference data is ${response.body()}")
                                    val practiceConcentractionDifference = response.body()!!.get("practice_concentraction_difference")
                                    if(practiceConcentractionDifference.asString.isNotEmpty()) {
                                        geniusPracticeData.concentractionScore = concentraction_score
                                        geniusPracticeData.concentractionDifference = practiceConcentractionDifference.asString
                                        UpdateRoomMethod().updateGeniusPracticeData(context = application.applicationContext, geniusPracticeData = geniusPracticeData)
                                    }
                                } catch (e : Exception) {
                                    Toast.makeText(application.applicationContext, "에러", Toast.LENGTH_LONG).show()
                                    Log.d("TAG", "postDataToServer PracticeDifference error is ${e.message}")
                                }
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

    fun getGeniusPracticeQuicknessDifference(quickness_score : String, pk : String, application: Application): LiveData<JsonObject?> {
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
                                val geniusPracticeData = GetRoomMethod().getGeniusPracticeData(application.applicationContext)
                                try {
                                    Log.d("TAG", "postDataToServer PracticeDifference data is ${response.body()}")
                                    val practiceQuicknessDifference = response.body()!!.get("practice_quickness_difference")
                                    if(practiceQuicknessDifference.asString.isNotEmpty()) {
                                        geniusPracticeData.quicknessScore = quickness_score
                                        geniusPracticeData.quicknessDifference = practiceQuicknessDifference.asString
                                        UpdateRoomMethod().updateGeniusPracticeData(context = application.applicationContext, geniusPracticeData = geniusPracticeData)
                                    }
                                } catch (e : Exception) {
                                    Toast.makeText(application.applicationContext, "에러", Toast.LENGTH_LONG).show()
                                    Log.d("TAG", "postDataToServer PracticeDifference error is ${e.message}")
                                }
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


    fun getGeniusTestMemoryDifference(memory_score : String, pk : String, application: Application): LiveData<JsonObject?> {
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
                                val geniusTestData = GetRoomMethod().getGeniusTestData(application.applicationContext)
                                try {
                                    Log.d("TAG", "postDataToServer testDifference data is ${response.body()}")
                                    val testMemoryDifference = response.body()!!.get("test_memory_difference")
                                    if(testMemoryDifference.asString.isNotEmpty()) {
                                        geniusTestData.memoryScore = memory_score
                                        geniusTestData.memoryDifference = testMemoryDifference.asString
                                        UpdateRoomMethod().updateGeniusTestData(context = application.applicationContext, geniusTestData = geniusTestData)
                                    }
                                } catch (e : Exception) {
                                    Toast.makeText(application.applicationContext, "에러", Toast.LENGTH_LONG).show()
                                    Log.d("TAG", "postDataToServer testDifference error is ${e.message}")
                                }
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

    fun getGeniusTestConcentractionDifference(concentraction_score : String, pk : String, application: Application): LiveData<JsonObject?> {
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
                                val geniusTestData = GetRoomMethod().getGeniusTestData(application.applicationContext)
                                try {
                                    Log.d("TAG", "postDataToServer testDifference data is ${response.body()}")
                                    val testConcentractionDifference = response.body()!!.get("test_concentraction_difference")
                                    if(testConcentractionDifference.asString.isNotEmpty()) {
                                        geniusTestData.concentractionScore = concentraction_score
                                        geniusTestData.concentractionDifference = testConcentractionDifference.asString
                                        UpdateRoomMethod().updateGeniusTestData(context = application.applicationContext, geniusTestData = geniusTestData)
                                    }
                                } catch (e : Exception) {
                                    Toast.makeText(application.applicationContext, "에러", Toast.LENGTH_LONG).show()
                                    Log.d("TAG", "postDataToServer testDifference error is ${e.message}")
                                }
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

    fun getGeniusTestQuicknessDifference(quickness_score : String, pk : String, application: Application): LiveData<JsonObject?> {
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
                                val geniusTestData = GetRoomMethod().getGeniusTestData(application.applicationContext)
                                try {
                                    Log.d("TAG", "postDataToServer TestDifference data is ${response.body()}")
                                    val TestQuicknessDifference = response.body()!!.get("test_quickness_difference")
                                    if(TestQuicknessDifference.asString.isNotEmpty()) {
                                        geniusTestData.quicknessScore = quickness_score
                                        geniusTestData.quicknessDifference = TestQuicknessDifference.asString
                                        UpdateRoomMethod().updateGeniusTestData(context = application.applicationContext, geniusTestData = geniusTestData)
                                    }
                                } catch (e : Exception) {
                                    Toast.makeText(application.applicationContext, "에러", Toast.LENGTH_LONG).show()
                                    Log.d("TAG", "postDataToServer TestDifference error is ${e.message}")
                                }
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


    fun getGeniusTestSumDifference(pk : String, application: Application): LiveData<JsonObject?> {
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
                                val testSumDifference = response.body()!!.get("test_sum_difference").asString
                                val geniusTestData = GetRoomMethod().getGeniusTestData(application.applicationContext)
                                when {
                                    testSumDifference.toFloat() < 0.3 -> {
                                        geniusTestData.level = "천재"
                                        UpdateRoomMethod().updateGeniusTestData(application.applicationContext, geniusTestData)
                                    }
                                    testSumDifference.toFloat() < 10 -> {
                                        geniusTestData.level = "고수"
                                        UpdateRoomMethod().updateGeniusTestData(application.applicationContext, geniusTestData)
                                    }
                                    testSumDifference.toFloat() < 50 -> {
                                        geniusTestData.level = "중수"
                                        UpdateRoomMethod().updateGeniusTestData(application.applicationContext, geniusTestData)
                                    }
                                    else -> {
                                        geniusTestData.level = "초보"
                                        UpdateRoomMethod().updateGeniusTestData(application.applicationContext, geniusTestData)
                                    }
                                }
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