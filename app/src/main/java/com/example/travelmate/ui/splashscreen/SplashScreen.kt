package com.example.travelmate.ui.splashscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelmate.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        addLogoAnimation()
    }

    private fun addLogoAnimation() {
        val view = fox
        view.animate().alpha(1.0f).setDuration(2000).start()
    }
}
