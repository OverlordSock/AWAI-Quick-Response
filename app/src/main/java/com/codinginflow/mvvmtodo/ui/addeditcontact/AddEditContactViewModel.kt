package com.codinginflow.mvvmtodo.ui.addeditcontact

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codinginflow.mvvmtodo.ADD_CONTACT_RESULT_OK
import com.codinginflow.mvvmtodo.EDIT_CONTACT_RESULT_OK
import com.codinginflow.mvvmtodo.data.Contact
import com.codinginflow.mvvmtodo.data.ContactDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditContactViewModel @ViewModelInject constructor(
    private val contactDao: ContactDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val contact = state.get<Contact>("contact")

    var contactName = state.get<String>("contactName") ?: contact?.name ?: ""
        set(value) {
            field = value
            state.set("contactName", value)
        }

    var contactPhone = state.get<String>("contactPhone") ?: contact?.phone ?: ""
        set(value) {
            field = value
            state.set("contactName", value)
        }

    var contactRelationship = state.get<String>("contactRelationship") ?: contact?.relationship ?: ""
        set(value) {
            field = value
            state.set("contactName", value)
        }

    var contactAppointed = state.get<Boolean>("contactAppointed") ?: contact?.appointed ?: false
        set(value) {
            field = value
            state.set("contactName", value)
        }

    private val addEditContactEventChannel = Channel<AddEditContactEvent>()
    val addEditContactEvent = addEditContactEventChannel.receiveAsFlow()

    fun onSaveClick() {
        if (contactName.isBlank()) {
            showInvalidInputMessage("Name cannot be empty")
            return
        } else if(contactPhone.isBlank()) {
            showInvalidInputMessage("Phone number cannot be empty")
            return
        } else if(contactRelationship.isBlank()) {
            showInvalidInputMessage("Relationship cannot be empty")
            return
        }
        if (contact != null) {
            val updatedContact = contact.copy(name = contactName, phone = contactPhone, relationship = contactRelationship, appointed = contactAppointed)
            updateContact(updatedContact)
        } else {
            val newContact = Contact(name = contactName, phone = contactPhone, relationship = contactRelationship, appointed = contactAppointed)
            createContact(newContact)
        }
    }

    private fun createContact(contact: Contact) = viewModelScope.launch {
        contactDao.insert(contact)
        addEditContactEventChannel.send(AddEditContactEvent.NavigateBackWithResult(ADD_CONTACT_RESULT_OK))
    }

    private fun updateContact(contact: Contact) = viewModelScope.launch {
        contactDao.update(contact)
        addEditContactEventChannel.send(AddEditContactEvent.NavigateBackWithResult(EDIT_CONTACT_RESULT_OK))
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditContactEventChannel.send(AddEditContactEvent.ShowInvalidInputMessage(text))
    }

    sealed class AddEditContactEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditContactEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditContactEvent()
    }
}