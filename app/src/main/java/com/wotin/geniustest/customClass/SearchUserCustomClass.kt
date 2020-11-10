package com.wotin.geniustest.customClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SearchUserCustomClass (
    @SerializedName("uniqueId")
    @Expose
    var currentSeeUserUID : String = "",

    @SerializedName("name")
    @Expose
    var name : String = "",

    @SerializedName("id")
    @Expose
    var id : String = "",

    var heart: Int = 0,

    @SerializedName("practice_memory_score")
    @Expose
    var practiceMemoryScore : String = "",

    @SerializedName("practice_memory_difference")
    @Expose
    var practiceMemoryDifference : String = "",

    @SerializedName("practice_concentraction_score")
    @Expose
    var practiceConcentractionScore : String = "",

    @SerializedName("practice_concentraction_difference")
    @Expose
    var practiceConcentractionDifference : String = "",

    @SerializedName("practice_quickness_score")
    @Expose
    var practiceQuicknessScore : String = "",

    @SerializedName("practice_quickness_difference")
    @Expose
    var practiceQuicknessDifference : String = "",

    @SerializedName("test_memory_score")
    @Expose
    var testMemoryScore : String = "",

    @SerializedName("test_memory_difference")
    @Expose
    var testMemoryDifference : String = "",

    @SerializedName("test_concentraction_score")
    @Expose
    var testConcentractionScore : String = "",

    @SerializedName("test_concentraction_difference")
    @Expose
    var testConcentractionDifference : String = "",

    @SerializedName("test_quickness_score")
    @Expose
    var testQuicknessScore : String = "",

    @SerializedName("test_quickness_difference")
    @Expose
    var testQuicknessDifference : String = "",

    @SerializedName("level")
    @Expose
    var level : String = "",

    var isHearted : Boolean = true
) {
    override fun toString(): String {
        return "currentSeeUserUID: $currentSeeUserUID, name: $name, id: $id, heart: $heart,\n practiceMemoryScore: $practiceMemoryScore, practiceConcentractionScore: $practiceConcentractionScore, practiceQuicknessScore: $practiceQuicknessScore, practiceMemoryDifference: $practiceMemoryDifference, practiceConcentractionDifference: $practiceConcentractionDifference, practiceQuicknessDifference: $practiceQuicknessDifference,\ntestMemoryScore: $testMemoryScore, testConcentractionScore: $testConcentractionScore, testQuicknessScore: $testQuicknessScore, testMemoryDifference: $testMemoryDifference, testConcentractionDifference: $testConcentractionDifference, testQuicknessDifference: $testQuicknessDifference,\nlevel: $level, isHearted: $isHearted"
    }
}