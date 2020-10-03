package com.wotin.geniustest.RoomMethod

import android.content.Context
import androidx.room.Room
import com.wotin.geniustest.CustomClass.GeniusPractice.GeniusPracticeDataCustomClass
import com.wotin.geniustest.CustomClass.GeniusTest.GeniusTestDataCustomClass
import com.wotin.geniustest.CustomClass.GeniusTest.TestModeCustomClass
import com.wotin.geniustest.DB.Genius.GeniusPracticeDataDB
import com.wotin.geniustest.DB.Genius.GeniusTestDataDB
import com.wotin.geniustest.DB.TestModeDB

class UpdateRoomMethod {

    fun updateTestModeData(
        context: Context,
        testMode : TestModeCustomClass
    ) {
        val testModeDB : TestModeDB = Room.databaseBuilder(
            context,
            TestModeDB::class.java, "testMode.db"
        )
            .allowMainThreadQueries()
            .build()
        testModeDB.testModeDB().updateTestMode(testMode)
    }

    fun updateGeniusPracticeData(context: Context, geniusPracticeData: GeniusPracticeDataCustomClass) {
        val geniusPracticeDB : GeniusPracticeDataDB = Room.databaseBuilder(
            context,
            GeniusPracticeDataDB::class.java, "geniusPractice.db"
        )
            .allowMainThreadQueries()
            . fallbackToDestructiveMigration ()
            .build()

        geniusPracticeDB.geniusPracticeDataDB().updateGeniusPracticeData(geniusPracticeData = geniusPracticeData)

    }

    fun updateGeniusTestData(context: Context, geniusTestData: GeniusTestDataCustomClass) {
        val geniusTestDataDB : GeniusTestDataDB = Room.databaseBuilder(
            context,
            GeniusTestDataDB::class.java, "geniusTest.db"
        )
            .allowMainThreadQueries()
            . fallbackToDestructiveMigration ()
            .build()
        geniusTestDataDB.geniusTestDataDB().updateGeniusTestData(geniusTestData)
    }

}
