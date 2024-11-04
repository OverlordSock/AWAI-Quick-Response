package com.codinginflow.mvvmtodo.ui.addeditprofile

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.databinding.FragmentAddEditProfileBinding
import com.codinginflow.mvvmtodo.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddEditProfileFragment : Fragment(R.layout.fragment_add_edit_profile) {

    private val viewModel: AddEditProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditProfileBinding.bind(view)

        binding.apply {
            editTextProfileName.setText(viewModel.profileName)
            editTextProfileAge.setText(viewModel.profileAge)
            editTextProfilePhone.setText(viewModel.profilePhone)
            editTextProfileAddress.setText(viewModel.profileAddress)
            editTextProfileCaregiver1.setText(viewModel.profileCaregiver1)
            editTextProfileCaregiver2.setText(viewModel.profileCaregiver2)

            editTextProfileName.addTextChangedListener {
                viewModel.profileName = it.toString()
            }

            editTextProfileAge.addTextChangedListener {
                viewModel.profileAge = it.toString()
            }

            editTextProfilePhone.addTextChangedListener {
                viewModel.profilePhone = it.toString()
            }

            editTextProfileAddress.addTextChangedListener {
                viewModel.profileAddress = it.toString()
            }

            editTextProfileCaregiver1.addTextChangedListener {
                viewModel.profileCaregiver1 = it.toString()
            }

            editTextProfileCaregiver2.addTextChangedListener {
                viewModel.profileCaregiver2 = it.toString()
            }

            fabSaveProfile.setOnClickListener {
                viewModel.onSaveClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditProfileEvent.collect { event ->
                when (event) {
                    is AddEditProfileViewModel.AddEditProfileEvent.NavigateBackWithResult -> {
                        binding.editTextProfileName.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                    is AddEditProfileViewModel.AddEditProfileEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }
        }
    }
}