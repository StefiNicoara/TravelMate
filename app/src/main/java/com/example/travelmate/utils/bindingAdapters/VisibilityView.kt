package com.example.travelmate.utils.bindingAdapters

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibilityView")
fun visibilityView(view: View, shouldBeVisible: Boolean?) {
    if (shouldBeVisible == true) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}