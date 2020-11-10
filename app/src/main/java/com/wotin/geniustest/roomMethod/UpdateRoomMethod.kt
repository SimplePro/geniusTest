package com.wotin.geniustest.roomMethod

import android.content.Context
import androidx.room.Room
import com.wotin.geniustest.customClass.geniusPractice.GeniusPracticeDataCustomClass
import com.wotin.geniustest.customClass.geniusTest.GeniusTestDataCustomClass
import com.wotin.geniustest.customClass.geniusTest.TestModeCustomClass
import com.wotin.geniustest.database.genius.GeniusPracticeDataDB
import com.wotin.geniustest.database.genius.GeniusTestDataDB
import com.wotin.geniustest.database.TestModeDB

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
