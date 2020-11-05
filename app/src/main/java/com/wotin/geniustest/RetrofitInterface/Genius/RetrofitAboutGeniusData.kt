package com.wotin.geniustest.RetrofitInterface.Genius

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitAboutGeniusData {

    @FormUrlEncoded
    @POST("genius_test/update_practice_memory_data/")
    suspend fun getGeniusPracticeMemoryDifference(
        @Field("memory_score") memory_score : String,
        @Field("pk") pk : String
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("genius_test/update_practice_concentraction_data/")
    suspend fun getGeniusPracticeConcentractionDifference(
        @Field("concentraction_score") concentraction_score : String,
        @Field("pk") pk : String
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("genius_test/update_practice_quickness_data/")
    suspend fun getGeniusPracticeQuicknessDifference(
        @Field("quickness_score") quickness_score : String,
        @Field("pk") pk : String
    ): Call<JsonObject>


    @FormUrlEncoded
    @POST("genius_test/update_test_memory_data/")
    suspend fun getGeniusTestMemoryDifference(
        @Field("memory_score") memory_score : String,
        @Field("pk") pk : String
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("genius_test/update_test_concentraction_data/")
    suspend fun getGeniusTestConcentractionDifference(
        @Field("concentraction_score") concentraction_score : String,
        @Field("pk") pk : String
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("genius_test/update_test_quickness_data/")
    suspend fun getGeniusTestQuicknessDifference(
        @Field("quickness_score") quickness_score : String,
        @Field("pk") pk : String
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("genius_test/test_sum_difference/")
    suspend fun getGeniusTestSumDifference(
        @Field("pk") pk : String
    ): Call<JsonObject>

}