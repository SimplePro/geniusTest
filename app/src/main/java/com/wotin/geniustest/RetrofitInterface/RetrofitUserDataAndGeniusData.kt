package com.wotin.geniustest.RetrofitInterface

import android.net.http.HttpResponseCache
import com.google.gson.JsonObject
import com.wotin.geniustest.CustomClass.RetrofitGetGeniusPracticeAndTestDataCustomClass
import com.wotin.geniustest.CustomClass.SignInAndSignUpCustomClass
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitUserDataAndGeniusData {
    @FormUrlEncoded
    @POST("genius_test/get_genius_test_data/")
    fun getGeniusData(
        @Field("pk") pk : String
    ): Call<RetrofitGetGeniusPracticeAndTestDataCustomClass>
}