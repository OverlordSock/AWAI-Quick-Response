package com.codinginflow.mvvmtodo.ui.addeditprofile

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codinginflow.mvvmtodo.ui.ADD_PROFILE_RESULT_OK
import com.codinginflow.mvvmtodo.ui.EDIT_PROFILE_RESULT_OK
import com.codinginflow.mvvmtodo.data.UserProfile
import com.codinginflow.mvvmtodo.data.UserProfileDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditProfileViewModel @ViewModelInject constructor(
    private val userProfileDao: UserProfileDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val profile = state.get<UserProfile>("profile")

    var profileName = state.get<String>("profileName") ?: profile?.name ?: ""
        set(value) {
            field = value
            state.set("profileName", value)
        }

    var profileAge = state.get<String>("profileAge") ?: profile?.age ?: ""
        set(value) {
            field = value
            state.set("profileName", value)
        }

    var profilePhone = state.get<String>("profilePhone") ?: profile?.phoneNumber ?: ""
        set(value) {
            field = value
            state.set("profileName", value)
        }

    var profileAddress = state.get<String>("profileAddress") ?: profile?.address ?: ""
        set(value) {
            field = value
            state.set("profileName", value)
        }

    var profileCaregiver1 = state.get<String>("profileCaregiver1") ?: profile?.caregiver1 ?: ""
        set(value) {
            field = value
            state.set("profileName", value)
        }

    var profileCaregiver2 = state.get<String>("profileCaregiver2") ?: profile?.caregiver2 ?: ""
        set(value) {
            field = value
            state.set("profileName", value)
        }

    private val addEditProfileEventChannel = Channel<AddEditProfileEvent>()
    val addEditProfileEvent = addEditProfileEventChannel.receiveAsFlow()

    fun onSaveClick() {
        if (profileName.isBlank()) {
            showInvalidInputMessage("Name cannot be empty")
            return
        } else if(profileAge.isBlank()) {
            showInvalidInputMessage("Age cannot be empty")
            return
        } else if(profilePhone.isBlank()) {
            showInvalidInputMessage("Phone number cannot be empty")
            return
        } else if(profileAddress.isBlank()) {
            showInvalidInputMessage("Relationship cannot be empty")
            return
        }else if(profileCaregiver1.isBlank()) {
            showInvalidInputMessage("Must have at least one caregiver")
            return
        }
        if (profile != null) {
            val updatedProfile = profile.copy(name = profileName, age = profileAge, phoneNumber = profilePhone,
                address = profileAddress, caregiver1 = profileCaregiver1, caregiver2 = profileCaregiver2)
            updateProfile(updatedProfile)
        } else {
            val newProfile = UserProfile(name = profileName, age = profileAge, phoneNumber = profilePhone,
                address = profileAddress, caregiver1 = profileCaregiver1, caregiver2 = profileCaregiver2)
            createProfile(newProfile)
        }
    }

    private fun createProfile(profile: UserProfile) = viewModelScope.launch {
        userProfileDao.insert(profile)
        addEditProfileEventChannel.send(AddEditProfileEvent.NavigateBackWithResult(
            ADD_PROFILE_RESULT_OK))
    }

    private fun updateProfile(profile: UserProfile) = viewModelScope.launch {
        userProfileDao.update(profile)
        addEditProfileEventChannel.send(AddEditProfileEvent.NavigateBackWithResult(EDIT_PROFILE_RESULT_OK))
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditProfileEventChannel.send(AddEditProfileEvent.ShowInvalidInputMessage(text))
    }

    sealed class AddEditProfileEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditProfileEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditProfileEvent()
    }
}