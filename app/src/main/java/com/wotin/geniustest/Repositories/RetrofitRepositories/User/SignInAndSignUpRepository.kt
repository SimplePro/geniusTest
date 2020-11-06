package com.wotin.geniustest.Repositories.RetrofitRepositories.User

import androidx.lifecycle.LiveData
import com.wotin.geniustest.CustomClass.SignInAndSignUpCustomClass
import com.wotin.geniustest.Repositories.RetrofitRepositories.Genius.GeniusDataRepository
import com.wotin.geniustest.RetrofitBuilder.GeniusRetrofitBuilder
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SignInAndSignUpRepository {
    var job : CompletableJob? = null

    fun signIn(id : String, password : String): LiveData<SignInAndSignUpCustomClass?> {
        job = Job()
        return object : LiveData<SignInAndSignUpCustomClass?>() {
            override fun onActive() {
                super.onActive()
                GeniusDataRepository.job?.let { theJob ->
                    CoroutineScope(Dispatchers.IO + theJob).launch {
                        var data : SignInAndSignUpCustomClass? = null
                        GeniusRetrofitBuilder.signInAndSignUpApiService.signIn(id, password).enqueue(object :
                            Callback<SignInAndSignUpCustomClass> {
                            override fun onFailure(call: Call<SignInAndSignUpCustomClass>, t: Throwable) {
                                data = null
                            }

                            override fun onResponse(
                                call: Call<SignInAndSignUpCustomClass>,
                                response: Response<SignInAndSignUpCustomClass>
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

    fun signUp(name : String, id : String, password : String, UniqueId : String): LiveData<SignInAndSignUpCustomClass?> {
        job = Job()
        return object : LiveData<SignInAndSignUpCustomClass?>() {
            override fun onActive() {
                super.onActive()
                GeniusDataRepository.job?.let { theJob ->
                    CoroutineScope(Dispatchers.IO + theJob).launch {
                        var data : SignInAndSignUpCustomClass? = null
                        GeniusRetrofitBuilder.signInAndSignUpApiService.signUp(name, id, password, UniqueId).enqueue(object :
                            Callback<SignInAndSignUpCustomClass> {
                            override fun onFailure(call: Call<SignInAndSignUpCustomClass>, t: Throwable) {
                                data = null
                            }

                            override fun onResponse(
                                call: Call<SignInAndSignUpCustomClass>,
                                response: Response<SignInAndSignUpCustomClass>
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