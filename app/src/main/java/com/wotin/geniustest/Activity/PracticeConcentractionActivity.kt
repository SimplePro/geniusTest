package com.wotin.geniustest.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.wotin.geniustest.Adapter.PracticeConcentractionRecyclerViewAdapter
import com.wotin.geniustest.R
import kotlinx.android.synthetic.main.activity_practice_concentraction.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max

class PracticeConcentractionActivity : AppCompatActivity(), PracticeConcentractionRecyclerViewAdapter.ItemClickListener {

    var score = 1
    var counter = 5000
    var itemCount = 49
    var concentractionList : ArrayList<String> = arrayListOf()
    lateinit var concentractionRecyclerViewAdapter : PracticeConcentractionRecyclerViewAdapter
    lateinit var t : Timer
    lateinit var tt : TimerTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_concentraction)

        practice_concentraction_timer_progressbar.min = 0

        setConcentractionList()

        val recyclerViewLayoutManager = GridLayoutManager(applicationContext, 10)
        practice_concentraction_recyclerview.apply {
            layoutManager = recyclerViewLayoutManager
            adapter = concentractionRecyclerViewAdapter
            setHasFixedSize(true)
        }

        prog()

        go_to_mainactivity_from_practice_concentraction_activity_imageview.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            startActivity(intent)
            finish()
        }

        practice_concentraction_result_confirm_button.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun restart() {
        score += 1
        practice_concentraction_score_textview.text = score.toString()
        practice_concentraction_result_textview.text = score.toString()
        if(score % 5 == 0) {
            resetRecyclerViewGridLayout()
        }
        setConcentractionList()
        t.cancel()
        tt.cancel()
        counter = (5000 - (score * 200))
        prog()
    }

    private fun prog() {
        t = Timer()
        practice_concentraction_timer_progressbar.max = counter
        tt = object : TimerTask() {
            override fun run() {
                counter -= 1
                practice_concentraction_timer_progressbar.progress = counter
                if((counter / 100) == 0) {
                    t.cancel()
                    tt.cancel()
                    runOnUiThread {
                        practice_concentraction_game_layout.visibility = View.GONE
                        practice_concentraction_result_layout.visibility = View.VISIBLE
                    }
                }
            }
        }

        t.schedule(tt, 0, 10)
    }

    override fun onBackPressed() {
        val intent = Intent(this, PracticeActivity::class.java)
        startActivity(intent)
        t.cancel()
        tt.cancel()
        finish()
    }

    override fun itemClicked(position: Int) {
        if(concentractionList[position] == "h" || concentractionList[position] == "j" || concentractionList[position] == "Y"
            || concentractionList[position] == "9" || concentractionList[position] == "C") {
            restart()
        }
        else counter -= 100
    }

    private fun setConcentractionList() {
        concentractionList.removeAll(concentractionList)
        when (Random().nextInt(5)) {
            0 -> {
                for(i in 0 .. itemCount) {
                    concentractionList.add("n")
                }
                val random2 = Random().nextInt(itemCount)
                concentractionList[random2] = "h"
            }
            1 -> {
                for(i in 0 .. itemCount) {
                    concentractionList.add("i")
                }
                val random2 = Random().nextInt(itemCount)
                concentractionList[random2] = "j"
            }
            2 -> {
                for(i in 0 .. itemCount) {
                    concentractionList.add("V")
                }
                val random2 = Random().nextInt(itemCount)
                concentractionList[random2] = "Y"
            }
            3 -> {
                for(i in 0 .. itemCount) {
                    concentractionList.add("q")
                }
                val random2 = Random().nextInt(itemCount)
                concentractionList[random2] = "9"
            }
            else -> {
                for(i in 0 .. itemCount) {
                    concentractionList.add("G")
                }
                val random2 = Random().nextInt(itemCount)
                concentractionList[random2] = "C"
            }
        }
        concentractionRecyclerViewAdapter = PracticeConcentractionRecyclerViewAdapter(concentractionList, this)
        practice_concentraction_recyclerview.apply {
            adapter = concentractionRecyclerViewAdapter
            setHasFixedSize(true)
        }
    }

    private fun resetRecyclerViewGridLayout() {
        when {
            score >= 20 -> {
                itemCount = 74
                val recyclerViewLayoutManager = GridLayoutManager(applicationContext, 15)
                practice_concentraction_recyclerview.layoutManager = recyclerViewLayoutManager
            }
            score >= 15 -> {
                itemCount = 64
                val recyclerViewLayoutManager = GridLayoutManager(applicationContext, 13)
                practice_concentraction_recyclerview.layoutManager = recyclerViewLayoutManager
            }
            score >= 10 -> {
                itemCount = 59
                val recyclerViewLayoutManager = GridLayoutManager(applicationContext, 12)
                practice_concentraction_recyclerview.layoutManager = recyclerViewLayoutManager
            } else -> {
            itemCount = 49
        }
        }
        concentractionRecyclerViewAdapter.notifyDataSetChanged()
        practice_concentraction_recyclerview.apply {
            adapter = concentractionRecyclerViewAdapter
            setHasFixedSize(true)
        }
    }

}