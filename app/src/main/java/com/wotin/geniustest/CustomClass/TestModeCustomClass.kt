package com.wotin.geniustest.CustomClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TestMode")
data class TestModeCustomClass (
    @ColumnInfo(name = "testMode") val mode : String = "",
    @ColumnInfo(name = "testScore") var score : Int = 0,
    @ColumnInfo(name = "testDifference") var difference : String = "",
    @ColumnInfo(name = "testStart") var start : Boolean = true,
    @PrimaryKey(autoGenerate = true) val primaryKey: Long = 0)