package com.wotin.geniustest.dao.Genius

import androidx.room.*
import com.wotin.geniustest.customClass.GeniusTest.GeniusTestDataCustomClass

@Dao
interface GeniusTestDataDao {

    @Query("select * from GeniusTestData")
    fun getAll() : GeniusTestDataCustomClass

    @Update
    fun updateGeniusTestData(geniusTestData: GeniusTestDataCustomClass)

    @Insert
    fun insertGeniusTestData(geniusTestData : GeniusTestDataCustomClass)

    @Query("DELETE FROM GeniusTestData")
    fun deleteGeniusTestData()

}