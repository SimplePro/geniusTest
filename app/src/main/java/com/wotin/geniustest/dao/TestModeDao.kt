package com.wotin.geniustest.dao

import androidx.room.*
import com.wotin.geniustest.customClass.geniusTest.TestModeCustomClass

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