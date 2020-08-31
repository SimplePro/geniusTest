package com.wotin.geniustest.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wotin.geniustest.Activity.PracticeActivity
import com.wotin.geniustest.R
import kotlinx.android.synthetic.main.fragment_practice.view.*

class PracticeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_practice, container, false)

        rootView.practice_play_imageView.setOnClickListener {
            val intent = Intent(requireContext(), PracticeActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }

        return rootView
    }

}