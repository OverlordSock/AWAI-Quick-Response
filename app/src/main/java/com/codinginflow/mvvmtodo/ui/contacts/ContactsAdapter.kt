package com.codinginflow.mvvmtodo.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.mvvmtodo.data.Contact
import com.codinginflow.mvvmtodo.databinding.ItemContactBinding

class ContactsAdapter(private val listener: OnItemClickListener) : ListAdapter<Contact, ContactsAdapter.ContactsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false) // Instance of ItemContactBinding
        return ContactsViewHolder(binding) // Pass instance to ContactsViewHolder constructor
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem) // Bind item at position
    }

    // Inner class can access class as though it were accessing it from the outside
    inner class ContactsViewHolder(private val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){ // Check if item is invalid
                        val contact = getItem(position)
                        listener.onItemClick(contact)
                    }
                }
            }
        }

        fun bind(contact: Contact) { // Bind contact attributes to item fields
            binding.apply {
                textViewName.text = contact.name
                textViewPhone.text = contact.phone
                textViewRelationship.text = contact.relationship
                textViewAppointed.isVisible = contact.appointed
            }
        }
    }

    interface OnItemClickListener { // Interface for items to implement
        fun onItemClick(contact: Contact)
    }

    class DiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean =
            oldItem == newItem
    }
}