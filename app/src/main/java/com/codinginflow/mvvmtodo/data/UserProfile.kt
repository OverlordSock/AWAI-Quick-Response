package com.codinginflow.mvvmtodo.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "user_table")
@Parcelize
data class UserProfile (
    val name: String,
    val age: String,
    val phoneNumber: String,
    val address: String,
    val caregiver1: String,
    val caregiver2: String?,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable