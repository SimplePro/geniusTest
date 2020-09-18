package com.wotin.geniustest.DB.Genius

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wotin.geniustest.CustomClass.GeniusTest.GeniusTestDataCustomClass
import com.wotin.geniustest.Dao.Genius.GeniusTestDataDao

@Database(entities = [GeniusTestDataCustomClass::class], version = 2)
abstract class GeniusTestDataDB : RoomDatabase() {
    abstract fun geniusTestDataDB() : GeniusTestDataDao
}