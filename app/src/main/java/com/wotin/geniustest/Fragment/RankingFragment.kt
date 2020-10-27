package com.wotin.geniustest.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.wotin.geniustest.Adapter.RankRecyclerViewAdapter
import com.wotin.geniustest.Converters.MapJsonConverter
import com.wotin.geniustest.CustomClass.GeniusPractice.GeniusPracticeDataCustomClass
import com.wotin.geniustest.CustomClass.RankCustomClass
import com.wotin.geniustest.R
import com.wotin.geniustest.RetrofitInterface.Genius.RetrofitAboutGeniusData
import com.wotin.geniustest.RetrofitInterface.RetrofitRanking
import kotlinx.android.synthetic.main.fragment_ranking.view.*
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RankingFragment : Fragment() {

    lateinit var geniusPracticeData : GeniusPracticeDataCustomClass
    lateinit var retrofit: Retrofit
    lateinit var getGeniusTestRankData: RetrofitRanking
    lateinit var okHttpClient: OkHttpClient
    val baseUrl = "http://118.32.174.85:8080"

    lateinit var rankingRecyclerViewAdapter : RankRecyclerViewAdapter
    var rankingList : ArrayList<RankCustomClass> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_ranking, container, false)

        okHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        getGeniusTestRankData = retrofit.create(RetrofitRanking::class.java)
        getGeniusTestRankData.getGeniusTestRankData().enqueue(object : Callback<JsonArray> {
            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.d("TAG","RankingFragment getGeniusTestRankData is failure $t")
            }

            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                for(i in response.body()!!) {
                    val jsonObj = JSONObject(i.toString())
                    val mapI = jsonObj.toMap()
                    rankingList.add(RankCustomClass(
                            UniqueId = mapI["UniqueId"].toString(),
                            id = mapI["id"].toString(),
                            level = mapI["level"].toString(),
                            heart = mapI["heart"].toString(),
                            bestScore = mapI["best_score"].toString(),
                            ranking = mapI["ranking"].toString()
                        )
                    )
                }
                rankingList.forEach {
                    Log.d("TAG", "getGeniusTestRankData rankingList - ${it.toString()}")
                }
                rankingRecyclerViewAdapter = RankRecyclerViewAdapter(rankList = rankingList)
                rootView.ranking_recyclerview.apply {
                    adapter = rankingRecyclerViewAdapter
                    layoutManager = LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
                    setHasFixedSize(true)
                }
            }
        })

        // Inflate the layout for this fragment
        return rootView
    }


    fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
        when (val value = this[it])
        {
            is JSONArray ->
            {
                val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
                JSONObject(map).toMap().values.toList()
            }
            is JSONObject -> value.toMap()
            JSONObject.NULL -> null
            else            -> value
        }
    }

}