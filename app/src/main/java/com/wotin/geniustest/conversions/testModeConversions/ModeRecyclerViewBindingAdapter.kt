package com.wotin.geniustest.conversions.testModeConversions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.wotin.geniustest.R

object ModeRecyclerViewBindingAdapter {
    @BindingAdapter("practice_mode_src")
    @JvmStatic
    fun practiceModeSrc(imageView: ImageView, mode : String) {
        when(mode){
            "기억력 테스트" -> imageView.setImageResource(R.drawable.memory)
            "집중력 테스트" -> imageView.setImageResource(R.drawable.concentration)
            "순발력 테스트" -> imageView.setImageResource(R.drawable.quickness)
        }
    }

    @BindingAdapter("is_test_start")
    @JvmStatic
    fun testIsStart(imageView: ImageView, start: Boolean) {
        when(start) {
            true -> imageView.setImageResource(R.drawable.start_circle)
            false -> imageView.setImageResource(R.drawable.no_start_circle)
        }
    }
}