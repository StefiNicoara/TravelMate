package com.example.travelmate.ui.account.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.travelmate.MainActivity
import com.example.travelmate.R
import com.example.travelmate.databinding.ActivityLoginBinding
import com.example.travelmate.utils.ANIMATION_DURATION
import com.example.travelmate.utils.BaseActivity
import com.example.travelmate.utils.Resource
import org.koin.android.ext.android.inject

class LoginActivity : BaseActivity() {

    private val viewModel by inject<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginBinding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        loginBinding.loginViewModel = viewModel


        addWelcomeAnimation(loginBinding.wbId, loginBinding.mateId)

        viewModel.logInResponse.observe(this, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    goToHomeScreen()
                }
                is Resource.Error -> {
                    displayError(response.error)
                }
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


