package com.wotin.geniustest.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wotin.geniustest.CustomClass.GeniusPractice.GeniusPracticeDataCustomClass
import com.wotin.geniustest.Dao.GeniusPracticeDataDao

@Database(entities = [GeniusPracticeDataCustomClass::class], version = 1)
abstract class GeniusPracticeDataDB : RoomDatabase() {
    abstract fun geniusPracticeDataDB() : GeniusPracticeDataDao
}