package com.wotin.geniustest.RetrofitInterface.User

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitSearchUserData {

    @JvmSuppressWildcards
    @GET("genius_test/get_user_and_genius_test_data/{id}/")
    suspend fun getUserData(
        @Path("id") id : String
    ): Call<JsonObject>

}