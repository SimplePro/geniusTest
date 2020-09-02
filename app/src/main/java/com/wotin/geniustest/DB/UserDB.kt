package com.wotin.geniustest.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wotin.geniustest.CustomClass.UserCustomClass
import com.wotin.geniustest.Dao.UserDao

@Database(entities = [UserCustomClass::class], version = 1)
abstract class UserDB : RoomDatabase() {
    abstract fun userDB() : UserDao
}