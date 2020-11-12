package com.wotin.geniustest.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class GeniusTestViewModel(application: Application) : AndroidViewModel(application) {
    val score = MutableLiveData<Int>()

    init {
        score.value = 1
    }

    fun plusScore() {
        score.value = score.value?.plus(1)
    }
}