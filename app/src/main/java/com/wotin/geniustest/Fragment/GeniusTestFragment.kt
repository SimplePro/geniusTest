package com.wotin.geniustest.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wotin.geniustest.Activity.Test.TestActivity
import com.wotin.geniustest.CustomClass.GeniusTest.GeniusTestDataCustomClass
import com.wotin.geniustest.R
import com.wotin.geniustest.getGeniusTestData
import kotlinx.android.synthetic.main.fragment_genius_test.view.*

class GeniusTestFragment : Fragment() {

    lateinit var geniusTestData : GeniusTestDataCustomClass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val rootView = inflater.inflate(R.layout.fragment_genius_test, container, false)
        geniusTestData = getGeniusTestData(activity!!.applicationContext)

        rootView.genius_test_all_difference_textview.text = (((geniusTestData.concentractionDifference.toFloat() + geniusTestData.memoryDifference.toFloat()) / 2.0f).toString())
        rootView.genius_test_level_textview.text = geniusTestData.level

        rootView.genius_test_play_imageView.setOnClickListener {
            val intent = Intent(requireContext(), TestActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }

        // Inflate the layout for this fragment
        return rootView
    }



}