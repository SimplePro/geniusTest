package com.wotin.geniustest.RetrofitInterface.Genius

import android.net.http.HttpResponseCache
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitAboutGeniusData {
    @FormUrlEncoded
    @POST("genius_test/update_practice_concentraction_data/")
    fun getGeniusPracticeConcentractionDifference(
        @Field("concentraction_score") concentraction_score : String,
        @Field("pk") pk : String
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("genius_test/update_practice_quickness_data/")
    fun getGeniusPracticeQuicknessDifference(
        @Field("quickness_score") quickness_score : String,
        @Field("pk") pk : String
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("genius_test/update_test_concentraction_data/")
    fun getGeniusTestConcentractionDifference(
        @Field("concentraction_score") concentraction_score : String,
        @Field("pk") pk : String
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("genius_test/update_test_quickness_data/")
    fun getGeniusTestQuicknessDifference(
        @Field("quickness_score") quickness_score : String,
        @Field("pk") pk : String
    ): Call<JsonObject>

}