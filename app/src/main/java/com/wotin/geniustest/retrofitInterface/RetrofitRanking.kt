package com.wotin.geniustest.retrofitInterface

import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.http.POST

interface RetrofitRanking {

    @POST("genius_test/get_genius_test_rank_data")
    fun getGeniusTestRankData(
    ): Call<JsonArray>

}