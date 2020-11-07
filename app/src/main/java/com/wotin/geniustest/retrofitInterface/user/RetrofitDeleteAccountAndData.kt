package com.wotin.geniustest.retrofitInterface.user

import android.net.http.HttpResponseCache
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitDeleteAccountAndData {

    @FormUrlEncoded
    @POST("genius_test/delete_all_data/")
    fun deleteAccountAndData(
        @Field("pk") pk : String
    ): Call<HttpResponseCache>

}