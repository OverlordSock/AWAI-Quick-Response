package com.codinginflow.mvvmtodo.ui.quiz

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codinginflow.mvvmtodo.data.ContactDao
import com.codinginflow.mvvmtodo.data.UserProfileDao
import kotlinx.coroutines.flow.firstOrNull

class ConfirmSendViewModel @ViewModelInject constructor(
    private val userProfileDao: UserProfileDao,
    private val ContactDao: ContactDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val profiles = userProfileDao.getProfiles()
    val contacts = ContactDao.getContacts()

    suspend fun getUserName(): String? {
        return profiles.firstOrNull()?.firstOrNull()?.name
    }

    suspend fun getAllPhoneNumbers(): List<String> {
        return contacts.firstOrNull()?.mapNotNull { it.phone } ?: emptyList()
    }
}