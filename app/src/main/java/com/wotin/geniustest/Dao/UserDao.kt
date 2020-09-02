package com.wotin.geniustest.Dao

import androidx.room.*
import com.wotin.geniustest.CustomClass.UserCustomClass

@Dao
interface UserDao {
    @Query("SELECT * from User")
    fun getAll(): UserCustomClass

    @Insert
    fun insertUser(user : UserCustomClass)

    @Delete
    fun deleteUser(user : UserCustomClass)

}