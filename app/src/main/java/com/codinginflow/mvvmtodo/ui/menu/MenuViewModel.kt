package com.codinginflow.mvvmtodo.ui.menu

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codinginflow.mvvmtodo.data.ContactDao
import com.codinginflow.mvvmtodo.data.UserProfileDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MenuViewModel @ViewModelInject constructor(
    private val userProfileDao: UserProfileDao,
    private val ContactDao: ContactDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val profiles = userProfileDao.getProfiles()
    val contacts = ContactDao.getContacts()

    suspend fun onQuizClick(): Boolean {
        if (profiles.firstOrNull()?.firstOrNull() == null) {
            showInvalidInputMessage("Please add a profile")
            return false
        }else if (contacts.firstOrNull()?.firstOrNull() == null) {
            showInvalidInputMessage("Please add a contact")
            return false
        }else {
            return true
        }
    }

    suspend fun isTamariki(): Boolean {
        return profiles.firstOrNull()?.firstOrNull()?.let { userProfile ->
            userProfile.age.toIntOrNull()?.let { age ->
                age <= 13
            } ?: false  // Return false if age cannot be parsed to an integer
        } ?: false
    }

    private val menuEventChannel = Channel<MenuViewModel.MenuEvent>()
    val menuEvent = menuEventChannel.receiveAsFlow()

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        menuEventChannel.send(MenuViewModel.MenuEvent.ShowInvalidInputMessage(text))
    }

    sealed class MenuEvent {
        data class ShowInvalidInputMessage(val msg: String) : MenuEvent()
    }
}