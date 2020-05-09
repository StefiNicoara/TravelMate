package com.example.travelmate.utils

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    fun displayError(error: AppError?) {
        if (error?.message != null) {
            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                this,
                error?.messageRes?.let { resources.getString(it) } ?: "Unknown error",
                Toast.LENGTH_SHORT).show()
        }
    }
}
