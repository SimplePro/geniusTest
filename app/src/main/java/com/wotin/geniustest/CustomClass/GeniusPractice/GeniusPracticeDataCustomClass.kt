package com.wotin.geniustest.CustomClass.GeniusPractice

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GeniusPracticeData")
    data class GeniusPracticeDataCustomClass(
    @ColumnInfo(name = "geniusPracticeUniqueId") val UniqueId : String,
    @ColumnInfo(name = "geniusPracticeConcentractionScore") var concentractionScore : String,
    @ColumnInfo(name = "geniusPracticeMemoryScore") var memoryScore : String,
    @ColumnInfo(name = "geniusPracticeConcentractionDifference") var concentractionDifference : String,
    @ColumnInfo(name = "geniusPracticeMemoryDifference") var memoryDifference : String,
    @PrimaryKey(autoGenerate = true) val primaryKey : Long = 0
)