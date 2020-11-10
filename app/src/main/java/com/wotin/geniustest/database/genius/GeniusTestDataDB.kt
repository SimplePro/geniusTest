package com.wotin.geniustest.database.genius

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wotin.geniustest.customClass.geniusTest.GeniusTestDataCustomClass
import com.wotin.geniustest.dao.genius.GeniusTestDataDao

@Database(entities = [GeniusTestDataCustomClass::class], version = 2)
abstract class GeniusTestDataDB : RoomDatabase() {
    abstract fun geniusTestDataDB() : GeniusTestDataDao
}