package com.example.travelmate.ui.account.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.travelmate.R
import com.example.travelmate.databinding.ActivityRegisterBinding
import com.example.travelmate.utils.ANIMATION_DURATION
import org.koin.android.ext.android.inject

class RegisterActivity : AppCompatActivity() {

    private val viewModel by inject<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val registerBinding: ActivityRegisterBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_register)
        registerBinding.registerViewModel = viewModel

        addWelcomeAnimation(registerBinding.wbId, registerBinding.mateId, registerBinding.planeId)
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
}
