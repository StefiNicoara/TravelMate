package com.example.travelmate.ui.profile.journeys

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentJourneysBinding
import com.example.travelmate.model.Journey
import com.example.travelmate.ui.profile.journeys.recyclerViewAdapters.JourneyRVAdapter
import com.example.travelmate.utils.Resource
import org.koin.android.ext.android.inject

class JourneysFragment : Fragment() {

    private val viewModel by inject<JourneysViewModel>()
    private lateinit var binding: FragmentJourneysBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_journeys, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.visibilityPending = false
        binding.visibilityCurrent = false
        binding.visibilityUpcoming = false
        binding.visibilityPast = false
        viewModel.loadPendingJourneys()
        viewModel.loadCurrentJourneys()
        viewModel.loadUpcomingJourneys()
        viewModel.loadPastJourneys()
        observePendingJourneys()
        observeCurrentJourneys()
        observeUpcomingJourneys()
        observePastJourneys()
        observeStartJourney()

        binding.button.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.navigation_add_journey)
        }
    }

    private fun observeStartJourney() {
        viewModel.startJourney.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    viewModel.loadCurrentJourneys()
                    viewModel.loadUpcomingJourneys()
                }
                is Resource.Error -> {
                    Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun observePendingJourneys() {
        viewModel.loadPendingJourneys.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    //result.data?.let { setUpPendingRV(it) }
                    binding.visibilityPending = result.data?.isEmpty() != true
                    binding.pendingJourneys.adapter?.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun observeCurrentJourneys() {
        viewModel.loadCurrentJourneys.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    result.data?.let { setJourneyRV(it, binding.currentJourneyRV, false) }
                    binding.visibilityCurrent = result.data?.isEmpty() != true
                    binding.currentJourneyRV.adapter?.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun observeUpcomingJourneys() {
        viewModel.loadUpcomingJourneys.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    result.data?.let { setJourneyRV(it, binding.upcomingJourneyRV, true) }
                    binding.visibilityUpcoming = result.data?.isEmpty() != true
                    binding.upcomingJourneyRV.adapter?.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun observePastJourneys() {
        viewModel.loadPastJourneys.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    result.data?.let { setJourneyRV(it, binding.pastJourneyRV, false) }
                    binding.visibilityPast = result.data?.isEmpty() != true
                    binding.pastJourneyRV.adapter?.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setJourneyRV(journeyList: List<Journey>, rv: RecyclerView, isUpcoming: Boolean) {
        val adapter = context?.let {
            JourneyRVAdapter(
                it,
                journeyList,
                viewModel,
                isUpcoming
            )
        }
        rv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv.adapter = adapter
    }
}
