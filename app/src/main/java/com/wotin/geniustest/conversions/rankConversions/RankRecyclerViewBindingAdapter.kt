package com.wotin.geniustest.conversions.rankConversions

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.wotin.geniustest.EncryptionAndDetoxification
import com.wotin.geniustest.R
import org.json.JSONObject

object RankRecyclerViewBindingAdapter {
    @BindingAdapter("set_rank_image")
    @JvmStatic
    fun setRankImage(imageView: ImageView, level: String) {
        when(level) {
            "천재" -> imageView.setImageResource(R.drawable.genius)
            "고수" -> imageView.setImageResource(R.drawable.good_brain)
            "중수" -> imageView.setImageResource(R.drawable.normal_brain)
            "초보" -> imageView.setImageResource(R.drawable.bad_brain)
            else -> imageView.setImageResource(R.drawable.bad_brain)
        }
    }

    @BindingAdapter("set_rank_difference")
    @JvmStatic
    fun setRankDifference(textView: TextView, bestScore : String) {
        textView.text = JSONObject(bestScore).get("test_sum_difference").toString()
    }

    @BindingAdapter("set_id_encryption")
    @JvmStatic
    fun setIdEncryption(textView: TextView, id : String) {
        textView.text = EncryptionAndDetoxification().encryptionAndDetoxification(id)
    }

}