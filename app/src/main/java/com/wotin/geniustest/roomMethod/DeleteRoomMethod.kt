package com.wotin.geniustest.roomMethod

import android.content.Context
import androidx.room.Room
import com.wotin.geniustest.database.Genius.GeniusPracticeDataDB
import com.wotin.geniustest.database.Genius.GeniusTestDataDB
import com.wotin.geniustest.database.TestModeDB

class DeleteRoomMethod {

    fun deleteUserDataAndGeniusTestAndPracticeData(context: Context) {
        UserRoomMethod().deleteUserData(context)
        deleteGeniusPracticeData(context)
        deleteGeniusTestData(context)
    }


    fun deleteGeniusPracticeData(context: Context) {
        val geniusPracticeDB : GeniusPracticeDataDB = Room.databaseBuilder(
            context,
            GeniusPracticeDataDB::class.java, "geniusPractice.db"
        )
            .allowMainThreadQueries()
            . fallbackToDestructiveMigration ()
            .build()
        geniusPracticeDB.geniusPracticeDataDB().deleteGeniusPracticeData()
    }

    fun deleteGeniusTestData(context: Context) {
        val geniusTestDB : GeniusTestDataDB = Room.databaseBuilder(
            context,
            GeniusTestDataDB::class.java, "geniusTest.db"
        )
            .allowMainThreadQueries()
            . fallbackToDestructiveMigration ()
            .build()
        geniusTestDB.geniusTestDataDB().deleteGeniusTestData()
    }


    fun deleteTestModeData(
        context: Context
    ) {
        val testModeDB : TestModeDB = Room.databaseBuilder(
            context,
            TestModeDB::class.java, "testMode.db"
        )
            .allowMainThreadQueries()
            .build()
        testModeDB.testModeDB().deleteTestMode()
    }

}
