package com.codinginflow.mvvmtodo.ui.quiz

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.databinding.FragmentQuiz3Binding
import kotlinx.android.synthetic.main.fragment_contacts.*

class Quiz3Fragment : Fragment(R.layout.fragment_quiz_3) {

    private val args: Quiz3FragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentQuiz3Binding.bind(view)
        val isTamariki = args.tamariki
        val answers = args.answers

        if (!isTamariki) {
            textViewTitle.text = "Are you or anyone else about to get hurt?"
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
            answers[2] = "Yes"
        }

        val action = Quiz3FragmentDirections.actionQuiz3FragmentToQuiz4Fragment(ageBoolean, answers)
        findNavController().navigate(action)
    }
}