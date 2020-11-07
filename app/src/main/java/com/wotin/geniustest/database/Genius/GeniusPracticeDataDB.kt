package com.wotin.geniustest.database.Genius

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wotin.geniustest.customClass.GeniusPractice.GeniusPracticeDataCustomClass
import com.wotin.geniustest.dao.Genius.GeniusPracticeDataDao

@Database(entities = [GeniusPracticeDataCustomClass::class], version = 2)
abstract class GeniusPracticeDataDB : RoomDatabase() {
    abstract fun geniusPracticeDataDB() : GeniusPracticeDataDao
}