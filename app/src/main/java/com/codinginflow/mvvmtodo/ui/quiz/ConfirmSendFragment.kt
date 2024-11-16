package com.codinginflow.mvvmtodo.ui.quiz

import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ConfirmSendFragment : Fragment(R.layout.fragment_confirm_send) {

    private val viewModel: ConfirmSendViewModel by viewModels()
    private val args: ConfirmSendFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentConfirmSendBinding.bind(view)
        var isTamariki = args.tamariki
        var answers = args.answers

        binding.apply {
            buttonYes.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.SEND_SMS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    prepareMessage(isTamariki, answers)
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

        var isTamariki = args.tamariki
        var answers = args.answers

        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            prepareMessage(isTamariki, answers)
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun prepareMessage(tamariki: Boolean, answers: Array<String>?) {

        var smsMessage = "Default message"

        lifecycleScope.launch {

            val name = viewModel.getUserName()
            val datetime = getDateTime()

            if (tamariki && answers != null) {

                val q1 = answers[0]
                val q2 = answers[1]
                val q3 = answers[2]
                val q4 = answers[3]
                val q5 = answers[4]

                smsMessage = """
                Quick Response Alert: 
                
                $name sent a quiz alert.
                
                Yelling? $q1
                Scared? $q2
                Hurt? $q3
                Breaking things? $q4
                Want to leave? $q5
                
                Alert sent at $datetime
                """.trimIndent()

            } else if (!tamariki && answers != null) {

                val q1 = answers[0]
                val q2 = answers[1]
                val q3 = answers[2]
                val q4 = answers[3]
                val q5 = answers[4]

                smsMessage = """
                Quick Response Alert:
                
                $name sent a quiz alert.
                
                Danger? $q1
                Yelling? $q2
                Hurt? $q3
                Need ambulance? $q4
                Need help? $q5
                
                Alert sent at $datetime
                """.trimIndent()

            } else {
                 smsMessage = """
                Quick Response Alert:
    
                $name has sent an emergency alert using the Panic Button.
    
                Alert sent at $datetime
                """.trimIndent()

            }

            sendSMS(smsMessage)
        }
    }

    fun sendSMS(smsMessage: String) {
        lifecycleScope.launch {
            val phones = viewModel.getAllPhoneNumbers()
            val smsManager: SmsManager = SmsManager.getDefault()

            if (smsMessage.isNotEmpty()) {
                for (phone in phones) {
                    try {
                        smsManager.sendTextMessage(phone, null, smsMessage, null, null)
                    } catch (e: Exception) {
                        Log.e("SMS", "Error sending SMS", e)
                        Toast.makeText(requireContext(), "Failed to send SMS", Toast.LENGTH_SHORT).show()
                    }
                }
                Toast.makeText(requireContext(), "Message sent", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
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