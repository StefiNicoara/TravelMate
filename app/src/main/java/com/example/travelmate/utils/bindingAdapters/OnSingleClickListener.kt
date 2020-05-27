package com.example.travelmate.utils.bindingAdapters

import android.view.View
import androidx.databinding.BindingAdapter
import java.util.concurrent.atomic.AtomicBoolean

@BindingAdapter("onSingleClick")
fun View.setOnSingleClickListener(clickListener: View.OnClickListener?) {
    clickListener?.also {
        setOnClickListener(OnSingleClickListener(it))
    } ?: setOnClickListener(null)
}

class OnSingleClickListener(
    private val clickListener: View.OnClickListener,
    private val intervalMs: Long = 1000L
) : View.OnClickListener {

    private var canClick = AtomicBoolean(true)
    override fun onClick(view: View?) {
        if (canClick.getAndSet(false)) {
            view?.run {
                postDelayed({ canClick.set(true) }, intervalMs)
                clickListener.onClick(view)
            }
        }
    }
}