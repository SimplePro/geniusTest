package com.wotin.geniustest.retrofitInterface

import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitAboutHeart {

    @FormUrlEncoded
    @POST("genius_test/get_heart_to_me_people")
    fun getHeartToMePeople(
        @Field("pk") pk : String
    ): Call<JsonArray>

    @FormUrlEncoded
    @POST("genius_test/get_heart_to_people")
    fun getHeartToPeople(
        @Field("pk") pk : String
    ): Call<JsonArray>

}