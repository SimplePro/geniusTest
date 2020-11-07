package com.wotin.geniustest.dao

import androidx.room.*
import com.wotin.geniustest.customClass.UserCustomClass

@Dao
interface UserDao {
    @Query("SELECT * from User")
    fun getAll(): UserCustomClass

    @Insert
    fun insertUser(user : UserCustomClass)

    @Query("DELETE FROM User")
    fun deleteUser()

}