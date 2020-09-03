package com.wotin.geniustest

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.wotin.geniustest.CustomClass.GeniusPractice.GeniusPracticeDataCustomClass
import com.wotin.geniustest.CustomClass.GeniusTest.GeniusTestDataCustomClass
import com.wotin.geniustest.CustomClass.UserCustomClass
import com.wotin.geniustest.DB.GeniusPracticeDataDB
import com.wotin.geniustest.DB.GeniusTestDataDB
import com.wotin.geniustest.DB.UserDB
import okhttp3.internal.connection.ConnectInterceptor

fun deleteUserDataAndGeniusTestData(context: Context) {
    deleteUserData(context)
    deleteGeniusPracticeData(context)
    deleteGeniusTestData(context)
}

fun deleteUserData(context: Context) {
    val userDB : UserDB = Room.databaseBuilder(
        context,
        UserDB::class.java, "user.db"
    ).allowMainThreadQueries()
        .build()
    val userData = userDB.userDB().getAll()
    userDB.userDB().deleteUser(userData)
}

fun deleteGeniusPracticeData(context: Context) {
    val geniusPracticeDB : GeniusPracticeDataDB = Room.databaseBuilder(
        context,
        GeniusPracticeDataDB::class.java, "geniusPractice.db"
    ).allowMainThreadQueries()
        .build()
    val geniusPracticeData = geniusPracticeDB.geniusPracticeDataDB().getAll()
    geniusPracticeDB.geniusPracticeDataDB().deleteGeniusPracticeData(geniusPracticeData)
}

fun deleteGeniusTestData(context: Context) {
    val geniusTestDB : GeniusTestDataDB = Room.databaseBuilder(
        context,
        GeniusTestDataDB::class.java, "geniusTest.db"
    ).allowMainThreadQueries()
        .build()
    val geniusTestData = geniusTestDB.geniusTestDataDB().getAll()
    geniusTestDB.geniusTestDataDB().deleteGeniusTestData(geniusTestData)
}


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
    Log.d("TAG", "insertGeniusPracticeData is $geniusPracticeData")
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

fun getGeniusPracticeData(
    context: Context
): GeniusPracticeDataCustomClass {
    val geniusPracticeDB : GeniusPracticeDataDB = Room.databaseBuilder(
        context,
        GeniusPracticeDataDB::class.java, "geniusPractice.db"
    ).allowMainThreadQueries()
        .build()

    Log.d("TAG", "getGeniusPracticeData is ${geniusPracticeDB.geniusPracticeDataDB().getAll()}")

    return geniusPracticeDB.geniusPracticeDataDB().getAll()
}

