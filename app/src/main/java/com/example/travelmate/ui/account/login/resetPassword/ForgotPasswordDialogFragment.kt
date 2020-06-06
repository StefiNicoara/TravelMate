package com.example.travelmate.ui.account.login.resetPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer

import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentForgotPasswordBinding
import com.example.travelmate.utils.Resource
import org.koin.android.ext.android.inject

class ForgotPasswordDialogFragment : DialogFragment() {

    private val viewModel by inject<ForgotPasswordViewModel>()
    private lateinit var binding: FragmentForgotPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false)

        dialog?.window?.setBackgroundDrawableResource(R.drawable.rounded_bg)
        sendEmail()
        observeEmailSent()

        return binding.root
    }

    private fun sendEmail() {
        binding.sendBtn.setOnClickListener {
            val email = binding.email.text.toString()
            viewModel.forgotPassword(email)
        }
    }

    private fun observeEmailSent() {
        viewModel.sendEmail.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    Toast.makeText(
                        context,
                        "An email to reset your password was sent to your address!",
                        Toast.LENGTH_LONG
                    ).show()
                    dismiss()
                }
                is Resource.Error -> {
                    Toast.makeText(context, it.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}
