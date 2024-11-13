package com.codinginflow.mvvmtodo.ui.contacts

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.data.Contact
import com.codinginflow.mvvmtodo.databinding.FragmentContactsBinding
import com.codinginflow.mvvmtodo.ui.contacts.ContactsAdapter
import com.codinginflow.mvvmtodo.ui.contacts.ContactsViewModel
import com.codinginflow.mvvmtodo.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ContactsFragment : Fragment(R.layout.fragment_contacts), ContactsAdapter.OnItemClickListener {

    private val viewModel: ContactsViewModel by viewModels() // Injected by dagger with @AndroidEntryPoint

    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentContactsBinding.bind(view)

        val contactAdapter = ContactsAdapter(this)

        binding.apply {
            recyclerViewContacts.apply {
                adapter = contactAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            fabAddContact.setOnClickListener {
                viewModel.onAddNewContactClick()
            }
        }

        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)
        }

        viewModel.contacts.observe(viewLifecycleOwner) {
            contactAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted { // Will suspend when fragment stopped
            viewModel.contactsEvent.collect { event ->
                when(event) { // Smart cast to event type not ContactsEvent type, extension for compile time safety
                    is ContactsViewModel.ContactsEvent.NavigateToAddContactScreen -> { // No Contact passed
                        val action = ContactsFragmentDirections.actionContactsFragmentToAddEditContactFragment(null, "New Contact")
                        findNavController().navigate(action)
                    }
                    is ContactsViewModel.ContactsEvent.NavigateToEditContactScreen -> { // Contact passed
                        val action = ContactsFragmentDirections.actionContactsFragmentToAddEditContactFragment(event.contact, "Edit Contact")
                        findNavController().navigate(action)
                    }
                    is ContactsViewModel.ContactsEvent.ShowContactSavedConfirmationMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                }.exhaustive
            }
        }

        setHasOptionsMenu(true) // Show menu options
    }

    override fun onItemClick(contact: Contact) {
        viewModel.onContactSelected(contact)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }
}