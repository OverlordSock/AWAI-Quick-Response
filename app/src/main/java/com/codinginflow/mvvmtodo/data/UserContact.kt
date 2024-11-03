package com.codinginflow.mvvmtodo.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "user_table")
@Parcelize
data class UserContact (
    @PrimaryKey val id: Int,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val emergencyContact1: String,
    val emergencyContact2: String,
    val emergencyContact3: String,
) : Parcelable