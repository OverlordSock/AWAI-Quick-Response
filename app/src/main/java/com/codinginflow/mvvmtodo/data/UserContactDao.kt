package com.codinginflow.mvvmtodo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface UserContactDao{

    @Insert
    fun insertUserContact(userContact: UserContact)

    @Update
    fun UpdateUserContact(userContact: UserContact)

    @Delete
    fun DeleteUserContact(userContact: UserContact)
}