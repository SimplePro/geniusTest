package com.wotin.geniustest.DB.Genius

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wotin.geniustest.CustomClass.GeniusPractice.GeniusPracticeDataCustomClass
import com.wotin.geniustest.Dao.Genius.GeniusPracticeDataDao

@Database(entities = [GeniusPracticeDataCustomClass::class], version = 2)
abstract class GeniusPracticeDataDB : RoomDatabase() {
    abstract fun geniusPracticeDataDB() : GeniusPracticeDataDao
}