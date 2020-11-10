package com.wotin.geniustest.repositories

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.wotin.geniustest.EncryptionAndDetoxification
import com.wotin.geniustest.converters.MapJsonConverter
import com.wotin.geniustest.customClass.SearchUserCustomClass
import com.wotin.geniustest.retrofitBuilder.GeniusRetrofitBuilder
import com.wotin.geniustest.roomMethod.UserRoomMethod
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SearchUserDataRepository {

    val data : MutableLiveData<SearchUserCustomClass> = MutableLiveData()

    fun getUserData(id : String, application: Application): MutableLiveData<SearchUserCustomClass> {

        GeniusRetrofitBuilder.searchUserDataApiService.getUserData(id).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("TAG", "SearchUserDataRepository onFailure error is $t")
            }

            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                val getData = response.body()!!
                Log.d("TAG", "onResponse: getData is $getData")
                var isHearted = false
                if (getData["name"].asString == "") Toast.makeText(application.applicationContext, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show()
                else {
                    val testData = MapJsonConverter().MapToJsonConverter(getData["test"].toString())
                    val practiceData =
                        MapJsonConverter().MapToJsonConverter(getData["practice"].toString())
                    if (getData["uniqueId"].asString != UserRoomMethod().getUserData(application.applicationContext).UniqueId) {
                        for (i in getData["peopleIdHeartToMe"].asJsonArray.toList()) {
                            if (i.asString == UserRoomMethod().getUserData(application.applicationContext).UniqueId) {
                                isHearted = true
                                break
                            } else {
                                isHearted = false
                            }
                        }
                    } else {
                        Log.d("TAG", "getData['uniqueId'].asString == UserRoomMethod().getUserData(application.applicationContext).UniqueId")
                        isHearted = true
                    }
                    data.value = SearchUserCustomClass(
                        currentSeeUserUID = getData["uniqueId"].asString,
                        name = getData["name"].asString,
                        id = EncryptionAndDetoxification().encryptionAndDetoxification(getData["id"].asString),
                        heart = getData["peopleIdHeartToMe"].asJsonArray.toList().size,
                        practiceMemoryScore = practiceData["practice_memory_score"].toString(),
                        practiceMemoryDifference = practiceData["practice_memory_difference"].toString(),
                        practiceConcentractionScore = practiceData["practice_concentraction_score"].toString(),
                        practiceConcentractionDifference = practiceData["practice_concentraction_difference"].toString(),
                        practiceQuicknessScore = practiceData["practice_quickness_score"].toString(),
                        practiceQuicknessDifference = practiceData["practice_quickness_difference"].toString(),
                        testMemoryScore = testData["test_memory_score"].toString(),
                        testMemoryDifference = testData["test_memory_difference"].toString(),
                        testConcentractionScore = testData["test_concentraction_score"].toString(),
                        testConcentractionDifference = testData["test_concentraction_difference"].toString(),
                        testQuicknessScore = testData["test_quickness_score"].toString(),
                        testQuicknessDifference = testData["test_quickness_difference"].toString(),
                        level = getData["level"].asString,
                        isHearted = isHearted
                    )
                    Log.d("TAG", "data is ${data.value}")
                }
            }
        })
        return data
    }
}