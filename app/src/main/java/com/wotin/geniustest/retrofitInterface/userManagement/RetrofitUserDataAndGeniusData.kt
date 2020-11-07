package com.wotin.geniustest.retrofitInterface.userManagement

import com.wotin.geniustest.customClass.RetrofitGetGeniusPracticeAndTestDataCustomClass
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