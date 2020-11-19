package com.wotin.geniustest

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.contentValuesOf
import com.google.gson.JsonObject
import com.wotin.geniustest.retrofitBuilder.GeniusRetrofitBuilder
import com.wotin.geniustest.roomMethod.UserRoomMethod
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppStorage(context: Context) {
    val uniqueId = UserRoomMethod().getUserData(context).UniqueId

    fun purchasedNoAds() : Boolean {
        var result = false
        GeniusRetrofitBuilder.geniusTestShopApiService.getPayNoAds(uniqueId).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                result = false
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                result = response.body()!!["payed_no_ads"].asBoolean
            }

        })
        return result
    }

    fun purchasedUnlimitedTry() : Boolean {
        var result = false
        GeniusRetrofitBuilder.geniusTestShopApiService.getPayUnlimitedTry(uniqueId).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                result = false
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                result = response.body()!!["payed_no_ads"].asBoolean
            }

        })
        return result
    }

    fun setPurchasedNoAds() {
        GeniusRetrofitBuilder.geniusTestShopApiService.payNoAds(uniqueId).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("TAG", "setPurchasedNoAds error is $t")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("TAG", "setPurchasedNoAds response.body()!! is ${response.body()!!}")
            }
        })
    }

    fun setPurchasedUnlimitedTry() {
        GeniusRetrofitBuilder.geniusTestShopApiService.payUnlimitedTry(uniqueId).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("TAG", "setPurchasedNoAds error is $t")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("TAG", "setPurchasedNoAds response.body()!! is ${response.body()!!}")
            }
        })
    }

    fun setPurchasedNoAdsAndUnlimitedTry() {
        GeniusRetrofitBuilder.geniusTestShopApiService.payAll(uniqueId).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("TAG", "setPurchasedNoAds error is $t")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("TAG", "setPurchasedNoAds response.body()!! is ${response.body()!!}")
            }
        })
    }

}