package com.wotin.geniustest.Activity.UserManagement

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wotin.geniustest.R
import kotlinx.android.synthetic.main.activity_user_information.*

class UserInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_information)

        user_heart_lottieanimation_among_user_information.setOnClickListener {
            val animator = ValueAnimator.ofFloat(0f, 0.5f).setDuration(500)
            animator.addUpdateListener { animation: ValueAnimator ->
                user_heart_lottieanimation_among_user_information.progress = animation.animatedValue as Float
            }
            animator.start()
        }

    }
}