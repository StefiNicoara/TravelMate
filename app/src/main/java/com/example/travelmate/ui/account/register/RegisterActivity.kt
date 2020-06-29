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
import com.example.travelmate.utils.*
import org.koin.android.ext.android.inject

class RegisterActivity : BaseActivity() {

    private val viewModel by inject<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val registerBinding: ActivityRegisterBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_register)
        registerBinding.registerViewModel = viewModel

        val loadingFragment = LoadingFragment()

        addWelcomeAnimation(registerBinding.wbId, registerBinding.mateId, registerBinding.planeId)

        viewModel.registerResponse.observe(this, Observer { response ->
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
