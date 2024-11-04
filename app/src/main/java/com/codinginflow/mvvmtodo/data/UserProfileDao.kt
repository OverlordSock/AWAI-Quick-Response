package com.codinginflow.mvvmtodo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao{

    @Query("SELECT * FROM user_table")
    fun getProfiles(): Flow<List<UserProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userProfile: UserProfile)

    @Update
    suspend fun update(userProfile: UserProfile)

    @Delete
    suspend fun delete(userProfile: UserProfile)
}