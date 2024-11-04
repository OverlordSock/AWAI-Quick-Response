package com.codinginflow.mvvmtodo.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "contact_table")
@Parcelize
data class Contact(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val relationship: String,
    val appointed: Boolean,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable





