package com.codinginflow.mvvmtodo.ui.quiz

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.databinding.FragmentQuiz1Binding
import kotlinx.android.synthetic.main.fragment_contacts.*

class Quiz1Fragment : Fragment(R.layout.fragment_quiz_1) {

    private val args: Quiz1FragmentArgs by navArgs()
    val isTamariki = args.tamariki

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentQuiz1Binding.bind(view)

        if (!isTamariki) {
            textViewTitle.text = "Are you or anyone else in danger?"
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
        val answers = Array(5) { "No" }

        if (questionAnswer) {
            answers[0] = "Yes"
        }

        val action = Quiz1FragmentDirections.actionQuiz1FragmentToQuiz2Fragment(isTamariki, answers)
        findNavController().navigate(action)
    }
}