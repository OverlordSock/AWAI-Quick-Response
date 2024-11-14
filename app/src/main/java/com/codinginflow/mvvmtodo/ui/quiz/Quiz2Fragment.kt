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
    val isTamariki = args.tamariki
    val answers = args.answers

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentQuiz2Binding.bind(view)

        if (!isTamariki) {
            textViewTitle.text = "Is anyone yelling or screaming?"
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
            answers[1] = "Yes"
        }

        val action = Quiz2FragmentDirections.actionQuiz2FragmentToQuiz3Fragment(isTamariki, answers)
        findNavController().navigate(action)
    }
}