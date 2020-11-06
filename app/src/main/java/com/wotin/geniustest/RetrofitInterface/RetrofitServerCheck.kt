package com.wotin.geniustest.RetrofitInterface

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.POST

interface RetrofitServerCheck {

    @POST("genius_test/server-check")
    suspend fun serverCheck(): Call<JsonObject>

}