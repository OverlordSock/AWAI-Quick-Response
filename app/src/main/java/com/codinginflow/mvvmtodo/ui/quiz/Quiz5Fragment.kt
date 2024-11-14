package com.codinginflow.mvvmtodo.ui.quiz

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.databinding.FragmentQuiz5Binding
import kotlinx.android.synthetic.main.fragment_contacts.*

class Quiz5Fragment : Fragment(R.layout.fragment_quiz_5) {

    private val args: Quiz5FragmentArgs by navArgs()
    val isTamariki = args.tamariki
    val answers = args.answers

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentQuiz5Binding.bind(view)

        if (!isTamariki) {
            textViewTitle.text = "Do you need immediate help?"
        }

        binding.apply {
            buttonYes.setOnClickListener {
                nextQuestion(true)
            }
            buttonNo.setOnClickListener {
                nextQuestion(false)
            }
        }
    }

    fun nextQuestion(questionAnswer: Boolean) {

        if (questionAnswer) {
            answers[4] = "Yes"
        }

        val action = Quiz5FragmentDirections.actionQuiz5FragmentToConfirmSendFragment(isTamariki, answers)
        findNavController().navigate(action)
    }
}