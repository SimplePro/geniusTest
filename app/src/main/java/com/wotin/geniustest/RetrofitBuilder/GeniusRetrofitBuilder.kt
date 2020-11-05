package com.wotin.geniustest.RetrofitBuilder

import com.wotin.geniustest.RetrofitInterface.Genius.RetrofitAboutGeniusData
import com.wotin.geniustest.RetrofitInterface.Genius.RetrofitPostUserHeart
import com.wotin.geniustest.RetrofitInterface.RetrofitAboutHeart
import com.wotin.geniustest.RetrofitInterface.RetrofitRanking
import com.wotin.geniustest.RetrofitInterface.RetrofitServerCheck
import com.wotin.geniustest.RetrofitInterface.User.RetrofitDeleteAccountAndData
import com.wotin.geniustest.RetrofitInterface.User.RetrofitSearchUserData
import com.wotin.geniustest.RetrofitInterface.User.RetrofitSignInAndSignUp
import com.wotin.geniustest.RetrofitInterface.UserManagement.RetrofitForgotIdAndPassword
import com.wotin.geniustest.RetrofitInterface.UserManagement.RetrofitUserDataAndGeniusData
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object GeniusRetrofitBuilder {

    const val BASE_URL = "http://118.32.174.85:8080"

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

}