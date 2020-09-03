package com.wotin.geniustest.CustomClass.GeniusTest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GeniusTestData")
data class GeniusTestDataCustomClass(
    @ColumnInfo(name = "geniusTestUniqueId") val UniqueId : String,
    @ColumnInfo(name = "geniusTestConcentractionScore") val concentractionScore : String,
    @ColumnInfo(name = "geniusTestMemoryScore") val memoryScore : String,
    @ColumnInfo(name = "geniusTestConcentractionDifference") val concentractionDifference : String,
    @ColumnInfo(name = "geniusTestMemoryDifference") val memoryDifference : String,
    @ColumnInfo(name = "geniusTestLevel") val level : String,
    @PrimaryKey(autoGenerate = true) val primaryKey : Long = 0
)