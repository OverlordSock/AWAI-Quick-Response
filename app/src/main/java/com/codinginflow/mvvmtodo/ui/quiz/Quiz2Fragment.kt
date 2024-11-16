package com.codinginflow.mvvmtodo.ui.quiz

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.databinding.FragmentQuiz2Binding
import kotlinx.android.synthetic.main.fragment_contacts.*

class Quiz2Fragment : Fragment(R.layout.fragment_quiz_2) {

    private val args: Quiz2FragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentQuiz2Binding.bind(view)
        val isTamariki = args.tamariki
        val answers = args.answers

        if (!isTamariki) {
            textViewTitle.text = "Is anyone yelling or screaming?"
        }

        binding.apply {
            buttonYes.setOnClickListener {
                nextQuestion(true, isTamariki, answers)
            }
            buttonNo.setOnClickListener {
                nextQuestion(false, isTamariki, answers)
            }
        }
    }

    fun nextQuestion(questionAnswer: Boolean, ageBoolean: Boolean, answers: Array<String>) {

        if (questionAnswer) {
            answers[1] = "Yes"
        }

        val action = Quiz2FragmentDirections.actionQuiz2FragmentToQuiz3Fragment(ageBoolean, answers)
        findNavController().navigate(action)
    }
}