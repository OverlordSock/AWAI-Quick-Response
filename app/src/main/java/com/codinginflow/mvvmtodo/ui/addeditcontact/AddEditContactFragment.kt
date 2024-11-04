package com.codinginflow.mvvmtodo.ui.addeditcontact

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.databinding.FragmentAddEditContactBinding
import com.codinginflow.mvvmtodo.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddEditContactFragment : Fragment(R.layout.fragment_add_edit_contact) {

    private val viewModel: AddEditContactViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditContactBinding.bind(view)

        binding.apply {
            editTextContactName.setText(viewModel.contactName)
            editTextContactRelationship.setText(viewModel.contactRelationship)
            editTextContactPhone.setText(viewModel.contactPhone)
            checkBoxContactAppointed.isChecked = viewModel.contactAppointed
            checkBoxContactAppointed.jumpDrawablesToCurrentState()

            editTextContactName.addTextChangedListener {
                viewModel.contactName = it.toString()
            }

            editTextContactRelationship.addTextChangedListener {
                viewModel.contactRelationship = it.toString()
            }

            editTextContactPhone.addTextChangedListener {
                viewModel.contactPhone = it.toString()
            }

            checkBoxContactAppointed.setOnCheckedChangeListener { _, isChecked ->
                viewModel.contactAppointed = isChecked
            }

            fabSaveContact.setOnClickListener {
                viewModel.onSaveClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditContactEvent.collect { event ->
                when (event) {
                    is AddEditContactViewModel.AddEditContactEvent.NavigateBackWithResult -> {
                        binding.editTextContactName.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                    is AddEditContactViewModel.AddEditContactEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }
        }
    }
}