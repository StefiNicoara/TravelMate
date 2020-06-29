package com.example.travelmate.ui.account.login

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.travelmate.MainActivity
import com.example.travelmate.R
import com.example.travelmate.databinding.ActivityLoginBinding
import com.example.travelmate.ui.account.login.resetPassword.ForgotPasswordDialogFragment
import com.example.travelmate.utils.*
import org.koin.android.ext.android.inject

class LoginActivity : BaseActivity() {

    private val viewModel by inject<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginBinding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        loginBinding.loginViewModel = viewModel

        val loadingFragment = LoadingFragment()

        addWelcomeAnimation(loginBinding.wbId, loginBinding.mateId)

        viewModel.logInResponse.observe(this, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    loadingFragment.show(supportFragmentManager, NEW_DIALOG)
                }
                is Resource.Success -> {
                    loadingFragment.dismiss()
                    goToHomeScreen()
                }
                is Resource.Error -> {
                    loadingFragment.dismiss()
                    displayError(response.error)
                }
            }
        })

        viewModel.resetPassword.observe(this, Observer { response ->
            if (response == true) {
                val dialog = ForgotPasswordDialogFragment()
                dialog.show(supportFragmentManager, NEW_DIALOG)
            }
        })
    }


    private fun addWelcomeAnimation(welcome: TextView, mate: TextView) {
        welcome.animate().alpha(1.0f).setDuration(ANIMATION_DURATION).start()

        mate.animate().alpha(1.0f).setDuration(ANIMATION_DURATION).setStartDelay(ANIMATION_DURATION)
            .start()
    }

    private fun goToHomeScreen() {
        intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}


