package com.wotin.geniustest.RetrofitInterface

import android.net.http.HttpResponseCache
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitAboutGeniusData {
    @FormUrlEncoded
    @POST("genius_test/update_practice_concentraction_data/")
    fun getGeniusPracticeDifference(
        @Field("concentraction_score") concentraction_score : String,
        @Field("pk") pk : String
    ): Call<JsonObject>
}