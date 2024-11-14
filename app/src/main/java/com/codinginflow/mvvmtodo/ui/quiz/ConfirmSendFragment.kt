package com.codinginflow.mvvmtodo.ui.quiz

import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.databinding.FragmentConfirmSendBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ConfirmSendFragment : Fragment(R.layout.fragment_confirm_send) {

    private val viewModel: ConfirmSendViewModel by viewModels()
    private val args: ConfirmSendFragmentArgs by navArgs()

    var isTamariki = args.tamariki
    var answers = args.answers
    var smsMessage = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentConfirmSendBinding.bind(view)

        binding.apply {
            buttonYes.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.SEND_SMS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    prepareMessage(isTamariki)
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(android.Manifest.permission.SEND_SMS),
                        100
                    )
                }
            }
            buttonNo.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            prepareMessage(isTamariki)
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun prepareMessage(tamariki: Boolean) {

        lifecycleScope.launch {

            val name = viewModel.getUserName()
            val datetime = getDateTime()

            if (answers == null) {
                smsMessage = """
                Quick Response Alert:
    
                $name has sent an emergency alert using the Panic Button.
    
                Alert sent at $datetime
                """.trimIndent()

            } else if (tamariki) {

                val q1 = answers!![0]
                val q2 = answers!![1]
                val q3 = answers!![2]
                val q4 = answers!![3]
                val q5 = answers!![4]

                smsMessage = """
                Quick Response Alert:
    
                $name has sent an alert with the Quick Response Quiz.
                Quiz Answers:
                
                Q: Is anyone yelling?
                A: $q1
                
                Q: Are you scared?
                A: $q2
                
                Q: Did anyone hurt you?
                A: $q3
                
                Q: Is someone breaking things?
                A: $q4
                
                Q: Do you want to get away?
                A: $q5
    
                Alert sent at $datetime
                """.trimIndent()

            } else if (!tamariki) {

                val q1 = answers!![0]
                val q2 = answers!![1]
                val q3 = answers!![2]
                val q4 = answers!![3]
                val q5 = answers!![4]

                smsMessage = """
                Quick Response Alert:
    
                $name has sent an alert with the Quick Response Quiz.
                Quiz Answers:
                
                Q: Are you or anyone else in danger?
                A: $q1
                
                Q: Is anyone yelling or screaming?
                A: $q2
                
                Q: Are you or anyone else about to get hurt?
                A: $q3
                
                Q: Does anyone need an ambulance?
                A: $q4
                
                Q: Do you need immediate help?
                A: $q5
    
                Alert sent at $datetime
                """.trimIndent()

            }

        }

        sendSMS()
    }

    fun sendSMS() {
        lifecycleScope.launch {
            val phones = viewModel.getAllPhoneNumbers()
            val smsManager: SmsManager = SmsManager.getDefault()

            if (smsMessage.isNotEmpty()) {
                for (phone in phones) {
                    smsManager.sendTextMessage(phone, null, smsMessage, null, null)
                }
                Toast.makeText(requireContext(), "Message sent", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun getDateTime(): String {
        val formatter = SimpleDateFormat("dd-MM HH:mm", Locale.getDefault())
        val current = Calendar.getInstance().time
        return formatter.format(current)
    }

}