package com.wotin.geniustest.Repositories.RetrofitRepositories.User

import android.net.http.HttpResponseCache
import androidx.lifecycle.LiveData
import com.wotin.geniustest.Repositories.RetrofitRepositories.Genius.GeniusDataRepository
import com.wotin.geniustest.RetrofitBuilder.GeniusRetrofitBuilder
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DeleteAccountAndDataRepository {
    var job : CompletableJob? = null

    fun deleteAccountAndData(pk : String): LiveData<HttpResponseCache?> {
        job = Job()
        return object : LiveData<HttpResponseCache?>() {
            override fun onActive() {
                super.onActive()
                GeniusDataRepository.job?.let { theJob ->
                    CoroutineScope(Dispatchers.IO + theJob).launch {
                        var data : HttpResponseCache? = null
                        GeniusRetrofitBuilder.deleteAccountApiService.deleteAccountAndData(pk).enqueue(object :
                            Callback<HttpResponseCache> {
                            override fun onFailure(call: Call<HttpResponseCache>, t: Throwable) {
                                data = null
                            }

                            override fun onResponse(
                                call: Call<HttpResponseCache>,
                                response: Response<HttpResponseCache>
                            ) {
                                data = response.body()
                            }

                        })
                        withContext(Dispatchers.Main) {
                            value = data
                            theJob.complete()
                        }
                    }
                }
            }
        }
    }

    fun cancelJobs() {
        job?.cancel()
    }
}