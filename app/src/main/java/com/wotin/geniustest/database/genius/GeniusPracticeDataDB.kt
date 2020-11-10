package com.wotin.geniustest.database.genius

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wotin.geniustest.customClass.geniusPractice.GeniusPracticeDataCustomClass
import com.wotin.geniustest.dao.genius.GeniusPracticeDataDao

@Database(entities = [GeniusPracticeDataCustomClass::class], version = 2)
abstract class GeniusPracticeDataDB : RoomDatabase() {
    abstract fun geniusPracticeDataDB() : GeniusPracticeDataDao
}