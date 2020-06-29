package com.example.travelmate.ui.profile.journeys.shareJourney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer

import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentShareJourneyDialogBinding
import com.example.travelmate.utils.ID_TAG
import com.example.travelmate.utils.Resource
import org.koin.android.ext.android.inject

class ShareJourneyDialogFragment : DialogFragment() {

    private val viewModel by inject<ShareJourneyViewModel>()
    private lateinit var binding: FragmentShareJourneyDialogBinding

    private var journeyId: String? = null
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            journeyId = it.getString(ID_TAG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_share_journey_dialog,
            container,
            false
        )
        dialog?.window?.setBackgroundDrawableResource(R.drawable.rounded_bg)
        binding.isFound = false
        binding.notFound = false
        binding.viewModel = viewModel
        search()
        observeSearch()
        send()
        observeSend()

        return binding.root
    }

    private fun search() {
        binding.searchBtn.setOnClickListener {
            viewModel.searchByUsername()
        }
    }

    private fun observeSearch() {
        viewModel.searchClick.observe(this, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    if (result.data != null) {
                        binding.isFound = true
                        binding.notFound = false
                        binding.userName = result.data.username
                        userId = result.data.userId
                    }
                }
                is Resource.Error -> {
                    binding.notFound = true
                    binding.isFound = false
                }
            }

        })
    }

    private fun send() {
        binding.sendBtn.setOnClickListener {
            journeyId?.let { it1 -> viewModel.sendToUser(it1, userId) }
        }
    }

    private fun observeSend() {
        viewModel.sendClick.observe(this, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    dismiss()
                    Toast.makeText(context, "Successfully sent!", Toast.LENGTH_LONG).show()
                }
                is Resource.Error -> {
                    binding.notFound = true
                    binding.isFound = false
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(journeyId: String) =
            ShareJourneyDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ID_TAG, journeyId)
                }
            }
    }
}
