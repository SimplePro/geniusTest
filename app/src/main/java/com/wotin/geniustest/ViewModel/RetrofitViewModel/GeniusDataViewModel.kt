package com.wotin.geniustest.ViewModel.RetrofitViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.wotin.geniustest.Repositories.RetrofitRepositories.Genius.GeniusDataRepository

class GeniusDataViewModel(application: Application) : AndroidViewModel(application) {

    fun getGeniusPracticeMemoryDifference(memory_score : String, pk : String) {
        GeniusDataRepository.getGeniusPracticeMemoryDifference(memory_score = memory_score, pk = pk, application = getApplication())
    }

    fun getGeniusPracticeConcentractionDifference(concentraction_score : String, pk : String) {
        GeniusDataRepository.getGeniusPracticeConcentractionDifference(concentraction_score = concentraction_score, pk = pk, application = getApplication())
    }

    fun getGeniusPracticeQuicknessDifference(quickness_score : String, pk : String){
        GeniusDataRepository.getGeniusPracticeQuicknessDifference(quickness_score = quickness_score, pk = pk, application = getApplication())
    }

    fun getGeniusTestMemoryDifference(memory_score: String, pk : String) {
        GeniusDataRepository.getGeniusTestMemoryDifference(memory_score = memory_score, pk = pk, application = getApplication())
    }

    fun getGeniusTestConcentractionDifference(concentraction_score : String, pk : String) {
        GeniusDataRepository.getGeniusTestConcentractionDifference(concentraction_score = concentraction_score, pk = pk, application = getApplication())
    }

    fun getGeniusTestQuicknessDifference(quickness_score : String, pk : String){
        GeniusDataRepository.getGeniusTestQuicknessDifference(quickness_score = quickness_score, pk = pk, application = getApplication())
    }

    fun getGeniusTestSumDifference(pk : String) {
        GeniusDataRepository.getGeniusTestSumDifference(pk = pk, application = getApplication())
    }
}