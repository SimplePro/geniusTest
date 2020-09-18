package com.wotin.geniustest.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wotin.geniustest.CustomClass.TestModeCustomClass
import com.wotin.geniustest.Dao.TestModeDao

@Database(entities = [TestModeCustomClass::class], version = 1)
abstract class TestModeDB : RoomDatabase() {
    abstract fun testModeDB() : TestModeDao
}