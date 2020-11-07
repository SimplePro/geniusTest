package com.wotin.geniustest.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonArray
import com.wotin.geniustest.adapter.RankRecyclerViewAdapter
import com.wotin.geniustest.customClass.GeniusPractice.GeniusPracticeDataCustomClass
import com.wotin.geniustest.customClass.RankCustomClass
import com.wotin.geniustest.R
import com.wotin.geniustest.retrofitBuilder.GeniusRetrofitBuilder
import kotlinx.android.synthetic.main.fragment_ranking.view.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RankingFragment : Fragment() {

    lateinit var geniusPracticeData : GeniusPracticeDataCustomClass

    lateinit var rankingRecyclerViewAdapter : RankRecyclerViewAdapter
    var rankingList : ArrayList<RankCustomClass> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_ranking, container, false)

        GeniusRetrofitBuilder.getGeniusTestRankDataApiService.getGeniusTestRankData().enqueue(object : Callback<JsonArray> {
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

    override fun onPause() {
        super.onPause()
        rankingList.removeAll(rankingList)
    }
}