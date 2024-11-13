package com.codinginflow.mvvmtodo.ui.contacts

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.codinginflow.mvvmtodo.data.*
import com.codinginflow.mvvmtodo.ui.ADD_CONTACT_RESULT_OK
import com.codinginflow.mvvmtodo.ui.EDIT_CONTACT_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ContactsViewModel @ViewModelInject constructor(
    private val contactDao: ContactDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val contactsEventChannel = Channel<ContactsEvent>() // Channel for sending action commands to fragment
    val contactsEvent = contactsEventChannel.receiveAsFlow() // Flow for handling channel to stop fragment sending to channel

    val contacts = contactDao.getContacts().asLiveData()

    fun onContactSelected(contact: Contact) = viewModelScope.launch { // Coroutine to send signal to navigate to edit fragment
        contactsEventChannel.send(ContactsEvent.NavigateToEditContactScreen(contact))
    }

    fun onAddNewContactClick() = viewModelScope.launch { // Coroutine to send signal to navigate to add fragment (no input given)
        contactsEventChannel.send(ContactsEvent.NavigateToAddContactScreen)
    }

    fun onAddEditResult(result: Int) {
        when (result) {
            ADD_CONTACT_RESULT_OK -> showContactSavedConfirmationMessage("Contact added")
            EDIT_CONTACT_RESULT_OK -> showContactSavedConfirmationMessage("Contact updated")
        }
    }

    private fun showContactSavedConfirmationMessage(text: String) = viewModelScope.launch {
        contactsEventChannel.send(ContactsEvent.ShowContactSavedConfirmationMessage(text))
    }

    // Sealed class to prepare values (events) to send through channel
    sealed class ContactsEvent {
        object NavigateToAddContactScreen : ContactsEvent()
        data class NavigateToEditContactScreen(val contact: Contact) : ContactsEvent()
        data class ShowContactSavedConfirmationMessage(val msg: String) : ContactsEvent()
    }
}