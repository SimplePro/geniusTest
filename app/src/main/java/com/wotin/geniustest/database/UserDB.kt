package com.wotin.geniustest.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wotin.geniustest.customClass.UserCustomClass
import com.wotin.geniustest.dao.UserDao

@Database(entities = [UserCustomClass::class], version = 1)
abstract class UserDB : RoomDatabase() {
    abstract fun userDB() : UserDao
}