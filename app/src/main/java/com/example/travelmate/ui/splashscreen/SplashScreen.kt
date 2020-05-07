package com.example.travelmate.ui.splashscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelmate.R
import com.example.travelmate.utils.ANIMATION_DURATION
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        addFoxAnimation()
        addPlaneAndLogoAnimation()
        addMottoAndButtonAnimation()
    }

    private fun addFoxAnimation() {
        val view = fox
        view.animate().alpha(1.0f).setDuration(ANIMATION_DURATION).setStartDelay(ANIMATION_DURATION).start()
    }

    private fun addPlaneAndLogoAnimation() {
        val viewPlane = plane
        viewPlane.animate().alpha(1.0f).setDuration(ANIMATION_DURATION).setStartDelay(2*ANIMATION_DURATION).start()
        val viewLogo = title_logo
        viewLogo.animate().alpha(1.0f).setDuration(ANIMATION_DURATION).setStartDelay(2*ANIMATION_DURATION).start()
    }

    private fun addMottoAndButtonAnimation() {
        val viewButton = logInButton
        viewButton.animate().alpha(1.0f).setDuration(ANIMATION_DURATION).setStartDelay(4*ANIMATION_DURATION).start()
        val viewNoAccount = no_account_layout
        viewNoAccount.animate().alpha(1.0f).setDuration(ANIMATION_DURATION).setStartDelay(4*ANIMATION_DURATION).start()
        val viewMotto = motto
        viewMotto.animate().alpha(1.0f).setDuration(ANIMATION_DURATION).setStartDelay(4*ANIMATION_DURATION).start()
    }

}
