package com.wotin.geniustest.RetrofitInterface.UserManagement

import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitForgotIdAndPassword {

    @FormUrlEncoded
    @POST("genius_test/get_id_for_name")
    suspend fun getIdForName(
        @Field("name") name : String
    ): Call<JsonArray>

}