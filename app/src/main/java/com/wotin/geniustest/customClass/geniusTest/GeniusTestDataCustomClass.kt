package com.wotin.geniustest.customClass.geniusTest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GeniusTestData")
data class GeniusTestDataCustomClass(
    @ColumnInfo(name = "geniusTestUniqueId") val UniqueId : String,
    @ColumnInfo(name = "geniusTestConcentractionScore") var concentractionScore : String = "1",
    @ColumnInfo(name = "geniusTestMemoryScore") var memoryScore : String = "1",
    @ColumnInfo(name = "geniusTestQuicknessScore") var quicknessScore : String = "1",
    @ColumnInfo(name = "geniusTestConcentractionDifference") var concentractionDifference : String = "99.9",
    @ColumnInfo(name = "geniusTestMemoryDifference") var memoryDifference : String = "99.9",
    @ColumnInfo(name = "geniusTestQuicknessDifference") var quicknessDifference : String = "99.9",
    @ColumnInfo(name = "geniusTestLevel") var level : String,
    @PrimaryKey(autoGenerate = true) val primaryKey : Long = 0
)