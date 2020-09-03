package com.wotin.geniustest

import android.content.Context
import androidx.room.Room
import com.wotin.geniustest.CustomClass.GeniusPractice.GeniusPracticeDataCustomClass
import com.wotin.geniustest.CustomClass.GeniusTest.GeniusTestDataCustomClass
import com.wotin.geniustest.CustomClass.UserCustomClass
import com.wotin.geniustest.DB.GeniusPracticeDataDB
import com.wotin.geniustest.DB.GeniusTestDataDB
import com.wotin.geniustest.DB.UserDB

fun insertUserData(name: String, id: String, password: String, UniqueId: String, context: Context) {
    val userDB: UserDB = Room.databaseBuilder(
        context,
        UserDB::class.java, "user.db"
    ).allowMainThreadQueries()
        .build()
    userDB.userDB().insertUser(UserCustomClass(name, id, password, UniqueId))
}

fun insertGeniusTestData(
    geniusTestData : GeniusTestDataCustomClass,
    context: Context
) {
    val geniusTestDataDB: GeniusTestDataDB = Room.databaseBuilder(
        context,
        GeniusTestDataDB::class.java, "geniusTest.db"
    ).allowMainThreadQueries()
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
    ).allowMainThreadQueries()
        .build()
    geniusPracticeDataDB.geniusPracticeDataDB().insertGeniusPracticeData(
        geniusPracticeData
    )
}

fun getGeniusTestData(
    context: Context
): GeniusTestDataCustomClass {
    val geniusTestDataDB : GeniusTestDataDB = Room.databaseBuilder(
        context,
        GeniusTestDataDB::class.java, "geniusTest.db"
    ).allowMainThreadQueries()
        .build()
    return geniusTestDataDB.geniusTestDataDB().getAll()
}

fun getUserGeniusPracticeData(
    context: Context
): GeniusPracticeDataCustomClass {
    val geniusPracticeDB : GeniusPracticeDataDB = Room.databaseBuilder(
        context,
        GeniusPracticeDataDB::class.java, "geniusPractice.db"
    ).allowMainThreadQueries()
        .build()

    return geniusPracticeDB.geniusPracticeDataDB().getAll()
}

