package com.wotin.geniustest.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wotin.geniustest.customClass.GeniusTest.TestModeCustomClass
import com.wotin.geniustest.dao.TestModeDao

@Database(entities = [TestModeCustomClass::class], version = 1)
abstract class TestModeDB : RoomDatabase() {
    abstract fun testModeDB() : TestModeDao
}