package com.wotin.geniustest.RoomMethod

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.wotin.geniustest.CustomClass.GeniusPractice.GeniusPracticeDataCustomClass
import com.wotin.geniustest.CustomClass.GeniusTest.GeniusTestDataCustomClass
import com.wotin.geniustest.CustomClass.TestModeCustomClass
import com.wotin.geniustest.DB.Genius.GeniusPracticeDataDB
import com.wotin.geniustest.DB.Genius.GeniusTestDataDB
import com.wotin.geniustest.DB.TestModeDB
import java.lang.Exception

class GetRoomMethod {

    lateinit var geniusPracticeDB : GeniusPracticeDataDB

    fun getGeniusTestData(
        context: Context
    ): GeniusTestDataCustomClass {
        val geniusTestDataDB : GeniusTestDataDB = Room.databaseBuilder(
            context,
            GeniusTestDataDB::class.java, "geniusTest.db"
        )
            .allowMainThreadQueries()
            . fallbackToDestructiveMigration ()
            .build()
        return geniusTestDataDB.geniusTestDataDB().getAll()
    }

    fun getGeniusPracticeData(
        context: Context
    ): GeniusPracticeDataCustomClass {
        try {
            geniusPracticeDB = Room.databaseBuilder(
                context,
                GeniusPracticeDataDB::class.java, "geniusPractice.db"
            )
                .allowMainThreadQueries()
                . fallbackToDestructiveMigration ()
                .build()
        } catch (e : Exception){
            Log.d("TAG", "getGeniusPracticeData: error is $e")
        }

        Log.d("TAG", "getGeniusPracticeData is ${geniusPracticeDB.geniusPracticeDataDB().getAll()}")

        return geniusPracticeDB.geniusPracticeDataDB().getAll()
    }

    fun getTestModeData(
        context: Context
    ): ArrayList<TestModeCustomClass> {
        val testModeDB : TestModeDB = Room.databaseBuilder(
            context,
            TestModeDB::class.java, "testMode.db"
        )
            .allowMainThreadQueries()
            .build()
        Log.d("TAG", "getTestModeData is ${testModeDB.testModeDB().getAllTestMode()}")

        return testModeDB.testModeDB().getAllTestMode() as ArrayList<TestModeCustomClass>
    }

}
