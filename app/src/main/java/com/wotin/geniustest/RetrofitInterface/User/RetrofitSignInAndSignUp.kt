package com.wotin.geniustest.RetrofitInterface.User

import com.wotin.geniustest.CustomClass.SignInAndSignUpCustomClass
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitSignInAndSignUp {

    @FormUrlEncoded
    @POST("genius_test/sign_in/")
    suspend fun signIn(
        @Field("id") id : String,
        @Field("password") password : String
    ): Call<SignInAndSignUpCustomClass>

    @FormUrlEncoded
    @POST("genius_test/sign_up/")
    suspend fun signUp(
        @Field("name") name : String,
        @Field("id") id : String,
        @Field("password") password : String,
        @Field("UniqueId") UniqueId : String
    ): Call<SignInAndSignUpCustomClass>

}