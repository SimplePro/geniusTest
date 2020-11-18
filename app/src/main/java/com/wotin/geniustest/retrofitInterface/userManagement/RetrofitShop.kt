package com.wotin.geniustest.retrofitInterface.userManagement

import com.google.gson.JsonObject
import com.wotin.geniustest.customClass.RetrofitGetGeniusPracticeAndTestDataCustomClass
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitShop {
    @FormUrlEncoded
    @POST("genius_test/genius_test_pay_no_ads")
    fun payNoAds(
        @Field("pk") pk : String
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("genius_test/genius_test_pay_unlimited_try")
    fun payUnlimitedTry(
        @Field("pk") pk : String
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("genius_test/genius_test_pay_all")
    fun payAll(
        @Field("pk") pk : String
    ): Call<JsonObject>
}