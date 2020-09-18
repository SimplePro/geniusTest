package com.wotin.geniustest.Dao

import androidx.room.*
import com.wotin.geniustest.CustomClass.TestModeCustomClass

@Dao
interface TestModeDao {
    @Query("SELECT * from TestMode")
    fun getAllTestMode() : List<TestModeCustomClass>

    @Update
    fun updateTestMode(testMode : TestModeCustomClass)

    @Insert
    fun insertTestMode(testMode : TestModeCustomClass)

    @Query("DELETE FROM TestMode")
    fun deleteTestMode()

}