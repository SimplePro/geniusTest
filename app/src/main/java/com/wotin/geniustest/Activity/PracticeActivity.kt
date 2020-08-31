package com.wotin.geniustest.Activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.wotin.geniustest.Adapter.ModeRecyclerViewAdapter
import com.wotin.geniustest.R
import kotlinx.android.synthetic.main.activity_practice.*

class PracticeActivity : AppCompatActivity() {

    lateinit var recyclerViewAdapter : ModeRecyclerViewAdapter
    val modeList = arrayListOf("기억력 테스트", "집중력 테스트")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)

        recyclerViewAdapter = ModeRecyclerViewAdapter(modeList)
        try {
            practice_mode_recyclerview.apply {
                adapter = recyclerViewAdapter
                layoutManager = LinearLayoutManager(this@PracticeActivity, LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
            }
        } catch (e : Exception) {
            Log.d("TAG", "error is $e")
        }
    }
}