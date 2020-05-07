package com.example.travelmate.ui.account.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelmate.R
import com.example.travelmate.utils.ANIMATION_DURATION
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        addWelcomeAnimation()
    }


    private fun addWelcomeAnimation() {
        val welcome = wbId
        welcome.animate().alpha(1.0f).setDuration(ANIMATION_DURATION).start()

        val mate = mateId
        mate.animate().alpha(1.0f).setDuration(ANIMATION_DURATION).setStartDelay(ANIMATION_DURATION).start()

        val plane = planeId
        plane.animate().alpha(1.0f).setDuration(ANIMATION_DURATION).setStartDelay(2*ANIMATION_DURATION).start()
    }
}


