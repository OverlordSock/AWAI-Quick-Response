package com.codinginflow.mvvmtodo.ui.quiz

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.databinding.FragmentQuiz4Binding
import kotlinx.android.synthetic.main.fragment_contacts.*

class Quiz4Fragment : Fragment(R.layout.fragment_quiz_4) {

    private val args: Quiz4FragmentArgs by navArgs()
    val isTamariki = args.tamariki
    val answers = args.answers

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentQuiz4Binding.bind(view)

        if (!isTamariki) {
            textViewTitle.text = "Does anyone need an ambulance?"
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
            answers[3] = "Yes"
        }

        val action = Quiz4FragmentDirections.actionQuiz4FragmentToQuiz5Fragment(isTamariki, answers)
        findNavController().navigate(action)
    }
}