package com.codinginflow.mvvmtodo.ui.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.databinding.FragmentMenuBinding
import com.codinginflow.mvvmtodo.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MenuFragment : Fragment(R.layout.fragment_menu) {

    private val viewModel: MenuViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMenuBinding.bind(view)

        binding.apply {

            buttonQuiz.setOnClickListener {
                lifecycleScope.launch {
                    if (viewModel.onQuizClick()) {
                        val action = MenuFragmentDirections.actionMenuFragmentToQuiz1Fragment(true)
                        findNavController().navigate(action)
                    }
                }
            }
            buttonProfile.setOnClickListener {
                val action = MenuFragmentDirections.actionMenuFragmentToAddEditProfileFragment()
                findNavController().navigate(action)
            }
            buttonContacts.setOnClickListener {
                val action = MenuFragmentDirections.actionMenuFragmentToContactsFragment()
                findNavController().navigate(action)
            }
            buttonPanic.setOnClickListener {
                lifecycleScope.launch {
                    if (viewModel.onQuizClick()) {
                        val action = MenuFragmentDirections.actionMenuFragmentToConfirmSendFragment(true)
                        findNavController().navigate(action)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.menuEvent.collect { event ->
                when (event) {
                    is MenuViewModel.MenuEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }
        }
    }
}