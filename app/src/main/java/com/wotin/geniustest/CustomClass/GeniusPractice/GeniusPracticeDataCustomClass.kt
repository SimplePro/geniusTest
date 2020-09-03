package com.wotin.geniustest.CustomClass.GeniusPractice

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GeniusPracticeData")
data class GeniusPracticeDataCustomClass(
    @ColumnInfo(name = "geniusPracticeUniqueId") val UniqueId : String,
    @ColumnInfo(name = "geniusPracticeConcentractionScore") val concentractionScore : String,
    @ColumnInfo(name = "geniusPracticeMemoryScore") val memoryScore : String,
    @ColumnInfo(name = "geniusPracticeConcentractionDifference") val concentractionDifference : String,
    @ColumnInfo(name = "geniusPracticeMemoryDifference") val memoryDifference : String,
    @PrimaryKey(autoGenerate = true) val primaryKey : Long = 0
)