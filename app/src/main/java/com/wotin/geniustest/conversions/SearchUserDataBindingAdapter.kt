package com.wotin.geniustest.conversions

import android.animation.ValueAnimator
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.wotin.geniustest.R

object SearchUserDataBindingAdapter {
    @BindingAdapter("is_heart")
    @JvmStatic
    fun setHeart(lottieAnimationView: LottieAnimationView, isHeart : String?) {
        if(!isHeart!!.toBoolean()) {
            val animator = ValueAnimator.ofFloat(0.9f, 0.0f).setDuration(700)
            animator.addUpdateListener { animation: ValueAnimator ->
                lottieAnimationView.progress = animation.animatedValue as Float
            }
            animator.start()
        } else {
            val animator = ValueAnimator.ofFloat(0.1f, 0.5f).setDuration(500)
            animator.addUpdateListener { animation: ValueAnimator ->
                lottieAnimationView.progress = animation.animatedValue as Float
            }
            animator.start()
        }
    }
    
    @BindingAdapter("is_level")
    @JvmStatic
    fun setLevel(imageView: ImageView, isLevel : String?) {
        when(isLevel) {
            "초보" -> imageView.setImageResource(R.drawable.bad_brain)
            "중수" -> imageView.setImageResource(R.drawable.normal_brain)
            "고수" -> imageView.setImageResource(R.drawable.good_brain)
            "천재" -> imageView.setImageResource(R.drawable.genius)
            else -> imageView.setImageResource(R.drawable.bad_brain)
        }
    }
}