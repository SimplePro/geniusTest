package com.wotin.geniustest.customClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class UserCustomClass(
    @ColumnInfo(name = "userName") var name : String = "",
    @ColumnInfo(name = "userId") var id : String = "",
    @ColumnInfo(name = "userPassword") var password : String  = "",
    @ColumnInfo(name = "userUniqueId") var UniqueId : String = "",
    @PrimaryKey(autoGenerate = true) val primaryKey : Long = 0
)