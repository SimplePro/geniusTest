package com.wotin.geniustest.customClass.geniusPractice

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GeniusPracticeData")
    data class GeniusPracticeDataCustomClass(
    @ColumnInfo(name = "geniusPracticeUniqueId") val UniqueId : String,
    @ColumnInfo(name = "geniusPracticeConcentractionScore") var concentractionScore : String = "1",
    @ColumnInfo(name = "geniusPracticeQuicknessScore") var quicknessScore : String = "1",
    @ColumnInfo(name = "geniusPracticeMemoryScore") var memoryScore : String = "1",
    @ColumnInfo(name = "geniusPracticeConcentractionDifference") var concentractionDifference : String = "99.9",
    @ColumnInfo(name = "geniusPracticeMemoryDifference") var memoryDifference : String = "99.9",
    @ColumnInfo(name = "geniusPracticeQuicknessDifference") var quicknessDifference : String = "99.9",
    @PrimaryKey(autoGenerate = true) val primaryKey : Long = 0
)