package com.example.travelmate.ui.account.register

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.travelmate.MainActivity
import com.example.travelmate.R
import com.example.travelmate.databinding.ActivityRegisterBinding
import com.example.travelmate.utils.ANIMATION_DURATION
import com.example.travelmate.utils.BaseActivity
import com.example.travelmate.utils.Resource
import org.koin.android.ext.android.inject

class RegisterActivity : BaseActivity() {

    private val viewModel by inject<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val registerBinding: ActivityRegisterBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_register)
        registerBinding.registerViewModel = viewModel

        addWelcomeAnimation(registerBinding.wbId, registerBinding.mateId, registerBinding.planeId)

        viewModel.registerResponse.observe(this, Observer { user ->
            when (user) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    goToHomeScreen()
                }
                is Resource.Error -> {
                    displayError(user.error)
                }
            }
        })
    }

    private fun addWelcomeAnimation(welcome: TextView, mate: TextView, plane: ImageView) {
        welcome.animate().alpha(1.0f)
            .setDuration(ANIMATION_DURATION)
            .start()

        mate.animate().alpha(1.0f)
            .setDuration(ANIMATION_DURATION)
            .setStartDelay(ANIMATION_DURATION)
            .start()

        plane.animate().alpha(1.0f)
            .setDuration(ANIMATION_DURATION)
            .setStartDelay(2 * ANIMATION_DURATION)
            .start()
    }

    private fun goToHomeScreen() {
        intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
