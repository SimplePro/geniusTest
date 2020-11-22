package com.wotin.geniustest.retrofitBuilder

import com.wotin.geniustest.retrofitInterface.genius.RetrofitAboutGeniusData
import com.wotin.geniustest.retrofitInterface.genius.RetrofitPostUserHeart
import com.wotin.geniustest.retrofitInterface.RetrofitAboutHeart
import com.wotin.geniustest.retrofitInterface.RetrofitRanking
import com.wotin.geniustest.retrofitInterface.RetrofitServerCheck
import com.wotin.geniustest.retrofitInterface.user.RetrofitDeleteAccountAndData
import com.wotin.geniustest.retrofitInterface.user.RetrofitSearchUserData
import com.wotin.geniustest.retrofitInterface.user.RetrofitSignInAndSignUp
import com.wotin.geniustest.retrofitInterface.userManagement.RetrofitForgotIdAndPassword
import com.wotin.geniustest.retrofitInterface.userManagement.RetrofitShop
import com.wotin.geniustest.retrofitInterface.userManagement.RetrofitUserDataAndGeniusData
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object GeniusRetrofitBuilder {

    const val BASE_URL = "http://172.22.137.99:8080"

    val okHttpClient : OkHttpClient.Builder by lazy {
        OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
    }

    val retrofitBuilder : Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build())
    }

    val signInAndSignUpApiService: RetrofitSignInAndSignUp by lazy {
        retrofitBuilder
            .build()
            .create(RetrofitSignInAndSignUp::class.java)
    }

    val getUserDataAndGeniusDataApiService : RetrofitUserDataAndGeniusData by lazy {
        retrofitBuilder
            .build()
            .create(RetrofitUserDataAndGeniusData::class.java)
    }

    val getAboutHeartApiService : RetrofitAboutHeart by lazy {
        retrofitBuilder
            .build()
            .create(RetrofitAboutHeart::class.java)
    }

    val geniusDataDifferenceApiService : RetrofitAboutGeniusData by lazy {
        retrofitBuilder
            .build()
            .create(RetrofitAboutGeniusData::class.java)
    }

    val deleteAccountApiService : RetrofitDeleteAccountAndData by lazy {
        retrofitBuilder
            .build()
            .create(RetrofitDeleteAccountAndData::class.java)
    }

    val searchUserDataApiService : RetrofitSearchUserData by lazy {
        retrofitBuilder
            .build()
            .create(RetrofitSearchUserData::class.java)
    }

    val postUserHeartApiService : RetrofitPostUserHeart by lazy {
        retrofitBuilder
            .build()
            .create(RetrofitPostUserHeart::class.java)
    }

    val forgotIdAndPasswordApiService : RetrofitForgotIdAndPassword by lazy {
        retrofitBuilder
            .build()
            .create(RetrofitForgotIdAndPassword::class.java)
    }

    val getGeniusTestRankDataApiService : RetrofitRanking by lazy {
        retrofitBuilder
            .build()
            .create(RetrofitRanking::class.java)
    }

    val geniusTestServerCheckApiService : RetrofitServerCheck by lazy {
        retrofitBuilder
            .build()
            .create(RetrofitServerCheck::class.java)
    }

    val geniusTestShopApiService : RetrofitShop by lazy {
        retrofitBuilder
            .build()
            .create(RetrofitShop::class.java)
    }

}