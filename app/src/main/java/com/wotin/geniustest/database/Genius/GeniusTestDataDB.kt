package com.wotin.geniustest.database.Genius

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wotin.geniustest.customClass.GeniusTest.GeniusTestDataCustomClass
import com.wotin.geniustest.dao.Genius.GeniusTestDataDao

@Database(entities = [GeniusTestDataCustomClass::class], version = 2)
abstract class GeniusTestDataDB : RoomDatabase() {
    abstract fun geniusTestDataDB() : GeniusTestDataDao
}