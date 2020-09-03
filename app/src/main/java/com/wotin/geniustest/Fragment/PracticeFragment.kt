package com.wotin.geniustest.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.wotin.geniustest.Activity.PracticeActivity
import com.wotin.geniustest.CustomClass.GeniusPractice.GeniusPracticeDataCustomClass
import com.wotin.geniustest.DB.GeniusPracticeDataDB
import com.wotin.geniustest.R
import com.wotin.geniustest.getGeniusPracticeData
import kotlinx.android.synthetic.main.fragment_practice.view.*

class PracticeFragment : Fragment() {

    lateinit var geniusPracticeData : GeniusPracticeDataCustomClass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        geniusPracticeData = getGeniusPracticeData(activity!!.applicationContext)
        val rootView = inflater.inflate(R.layout.fragment_practice, container, false)

        Log.d("TAG", "((geniusPracticeData.concentractionDifference.toFloat() + geniusPracticeData.memoryDifference.toFloat() / 2).toString()) is " +
                (((geniusPracticeData.concentractionDifference.toFloat() + geniusPracticeData.memoryDifference.toFloat()) / 2.0f).toString()))

        rootView.practice_all_difference_textview.text = (((geniusPracticeData.concentractionDifference.toFloat() + geniusPracticeData.memoryDifference.toFloat()) / 2.0f).toString())

        rootView.practice_play_imageView.setOnClickListener {
            val intent = Intent(requireContext(), PracticeActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }

        return rootView
    }



}