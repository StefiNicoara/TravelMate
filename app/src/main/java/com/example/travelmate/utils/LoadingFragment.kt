package com.example.travelmate.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment

import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentLoadingBinding

open class LoadingFragment : DialogFragment() {
    private lateinit var loadingIndicator: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val loadingBinding: FragmentLoadingBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(inflater.context),
                R.layout.fragment_loading,
                container,
                false
            )
        loadingIndicator = loadingBinding.loadingCircle
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        isCancelable = false
        return loadingBinding.root
    }
}