package com.wotin.geniustest

import android.content.Context
import androidx.room.Room
import com.wotin.geniustest.CustomClass.UserCustomClass
import com.wotin.geniustest.DB.UserDB

fun deleteUserData(context: Context) {
    val userDB : UserDB = Room.databaseBuilder(
        context,
        UserDB::class.java, "user.db"
    ).allowMainThreadQueries()
        .build()
    val userData = userDB.userDB().getAll()
    userDB.userDB().deleteUser(userData)
}

fun insertUserData(name: String, id: String, password: String, UniqueId: String, context: Context) {
    val userDB: UserDB = Room.databaseBuilder(
        context,
        UserDB::class.java, "user.db"
    ).allowMainThreadQueries()
        .build()
    userDB.userDB().insertUser(UserCustomClass(name, id, password, UniqueId))
}