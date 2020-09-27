package com.wotin.geniustest.RoomMethod

import android.content.Context
import androidx.room.Room
import com.wotin.geniustest.CustomClass.UserCustomClass
import com.wotin.geniustest.DB.UserDB

class UserRoomMethod {

    fun deleteUserData(context: Context) {
        val userDB : UserDB = Room.databaseBuilder(
            context,
            UserDB::class.java, "user.db"
        )
            .allowMainThreadQueries()
            . fallbackToDestructiveMigration ()
            .build()
        userDB.userDB().deleteUser()
    }

    fun insertUserData(name: String, id: String, password: String, UniqueId: String, context: Context) {
        val userDB: UserDB = Room.databaseBuilder(
            context,
            UserDB::class.java, "user.db"
        )
            .allowMainThreadQueries()
            . fallbackToDestructiveMigration ()
            .build()
        userDB.userDB().insertUser(UserCustomClass(name, id, password, UniqueId))
    }

    fun getUserData(context: Context): UserCustomClass {
        val userDB: UserDB = Room.databaseBuilder(
            context,
            UserDB::class.java, "user.db"
        )
            .allowMainThreadQueries()
            . fallbackToDestructiveMigration ()
            .build()
        return userDB.userDB().getAll()
    }

}