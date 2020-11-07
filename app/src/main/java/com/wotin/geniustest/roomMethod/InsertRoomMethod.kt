package com.wotin.geniustest.roomMethod

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.wotin.geniustest.customClass.GeniusPractice.GeniusPracticeDataCustomClass
import com.wotin.geniustest.customClass.GeniusTest.GeniusTestDataCustomClass
import com.wotin.geniustest.customClass.GeniusTest.TestModeCustomClass
import com.wotin.geniustest.database.Genius.GeniusPracticeDataDB
import com.wotin.geniustest.database.Genius.GeniusTestDataDB
import com.wotin.geniustest.database.TestModeDB

class InsertRoomMethod {

    fun insertGeniusTestData(
        geniusTestData : GeniusTestDataCustomClass,
        context: Context
    ) {
        val geniusTestDataDB: GeniusTestDataDB = Room.databaseBuilder(
            context,
            GeniusTestDataDB::class.java, "geniusTest.db"
        )
            .allowMainThreadQueries()
            . fallbackToDestructiveMigration ()
            .build()
        geniusTestDataDB.geniusTestDataDB().insertGeniusTestData(
            geniusTestData
        )
    }

    fun insertGeniusPracticeData(
        geniusPracticeData : GeniusPracticeDataCustomClass,
        context: Context
    ) {
        val geniusPracticeDataDB : GeniusPracticeDataDB = Room.databaseBuilder(
            context,
            GeniusPracticeDataDB::class.java, "geniusPractice.db"
        )
            .allowMainThreadQueries()
            . fallbackToDestructiveMigration ()
            .build()
        geniusPracticeDataDB.geniusPracticeDataDB().insertGeniusPracticeData(
            geniusPracticeData
        )
        Log.d("TAG", "insertGeniusPracticeData is $geniusPracticeData")
    }


    fun insertTestModeData(
        context: Context
    ) {
        val testModeDB : TestModeDB = Room.databaseBuilder(
            context,
            TestModeDB::class.java, "testMode.db"
        )
            .allowMainThreadQueries()
            .build()
        testModeDB.testModeDB().insertTestMode(
            TestModeCustomClass(
                "기억력 테스트",
                score = 1,
                difference = "99.9%"
            )
        )
        testModeDB.testModeDB().insertTestMode(
            TestModeCustomClass(
                "집중력 테스트",
                score = 1,
                difference = "99.9%"
            )
        )
        testModeDB.testModeDB().insertTestMode(
            TestModeCustomClass(
                "순발력 테스트",
                score = 1,
                difference = "99.9%"
            )
        )
    }

}
