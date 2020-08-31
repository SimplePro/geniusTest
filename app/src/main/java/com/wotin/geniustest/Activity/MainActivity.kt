package com.wotin.geniustest.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.wotin.geniustest.R
import com.wotin.geniustest.Adapter.TabLayoutFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 3초마다 윈도우 조정해주는 메소드 실행.
        controlWindowOnTimer()

        fragment_view_pager.adapter =
            TabLayoutFragmentPagerAdapter(
                supportFragmentManager
            )
        tab_layout.setupWithViewPager(fragment_view_pager)
        for(i in 0 until tab_layout.tabCount) {
            when(i) {
                0 -> {
                    tab_layout.getTabAt(i)!!.setIcon(R.drawable.practice)
                    tab_layout.getTabAt(i)!!.setText("연습하기")
                }
                1 -> {
                    tab_layout.getTabAt(i)!!.setIcon(R.drawable.genius)
                    tab_layout.getTabAt(i)!!.setText("천재 테스트")
                }
                2 -> {
                    tab_layout.getTabAt(i)!!.setIcon(R.drawable.ranking)
                    tab_layout.getTabAt(i)!!.setText("랭킹")
                }
            }
        }

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                val pos  = p0!!.position
                when(pos) {
                    0 -> {

                    }

                    1 ->  {

                    }

                    2 -> {

                    }

                }
            }

        })
    }

    //3초마다 윈도우를 조정해주는 메소드.
    private fun controlWindowOnTimer() {
        timer(period = 3000)
        {
            runOnUiThread {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            }
        }
    }
}