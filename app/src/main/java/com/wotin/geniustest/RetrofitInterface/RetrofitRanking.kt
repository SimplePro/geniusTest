package com.wotin.geniustest.RetrofitInterface

import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitRanking {

    @POST("genius_test/get_genius_test_rank_data")
    suspend fun getGeniusTestRankData(
    ): Call<JsonArray>

}