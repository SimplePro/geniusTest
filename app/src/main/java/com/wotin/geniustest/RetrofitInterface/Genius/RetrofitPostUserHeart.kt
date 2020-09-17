package com.wotin.geniustest.RetrofitInterface.Genius

import android.net.http.HttpResponseCache
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitPostUserHeart {
    @FormUrlEncoded
    @POST("genius_test/plus_heart/")
    fun plusHeart(
        @Field("current_uniqueId") currentUniqueId : String,
        @Field("heart_to_uniqueId") heartToUniqueId : String
    ): Call<HttpResponseCache>

    @FormUrlEncoded
    @POST("genius_test/minus_heart/")
    fun minusHeart(
        @Field("current_uniqueId") currentUniqueId : String,
        @Field("heart_to_uniqueId") heartToUniqueId : String
    ): Call<HttpResponseCache>


}
