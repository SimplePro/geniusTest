package com.wotin.geniustest.Dao

import androidx.room.*
import com.wotin.geniustest.CustomClass.GeniusTest.GeniusTestDataCustomClass

@Dao
interface GeniusTestDataDao {

    @Query("select * from GeniusTestData")
    fun getAll() : GeniusTestDataCustomClass

    @Update
    fun updateGeniusTestData(geniusTestData: GeniusTestDataCustomClass)

    @Insert
    fun insertGeniusTestData(geniusTestData : GeniusTestDataCustomClass)

    @Delete
    fun deleteGeniusTestData(geniusTestData: GeniusTestDataCustomClass)

}