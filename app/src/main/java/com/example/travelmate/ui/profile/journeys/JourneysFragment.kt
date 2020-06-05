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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentJourneysBinding
import com.example.travelmate.model.Journey
import com.example.travelmate.ui.profile.ProfileFragmentDirections
import com.example.travelmate.ui.profile.journeys.recyclerViewAdapters.JourneyRVAdapter
import com.example.travelmate.ui.profile.journeys.recyclerViewAdapters.PendingJourneysRVAdapter
import com.example.travelmate.ui.profile.journeys.shareJourney.ShareJourneyDialogFragment
import com.example.travelmate.utils.NEW_DIALOG
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
        observeNavigation()
        observeShareJourney()
        observeAcceptJourney()
        observeDeclineJourney()
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

    private fun observeShareJourney() {
        viewModel.shareJourney.observe(this, Observer { journeyId ->
            if (journeyId != null) {
                val dialog = ShareJourneyDialogFragment.newInstance(journeyId)
                dialog.show(childFragmentManager, NEW_DIALOG)
            }
        })
    }

    private fun observeAcceptJourney() {
        viewModel.acceptJourney.observe(this, Observer { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    viewModel.loadPendingJourneys()
                    viewModel.loadCurrentJourneys()
                    viewModel.loadUpcomingJourneys()
                    viewModel.loadPastJourneys()
                    Toast.makeText(context, "Accepted!", Toast.LENGTH_LONG).show()
                }
                is Resource.Error -> {
                    Toast.makeText(context, response.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun observeDeclineJourney() {
        viewModel.declineJourney.observe(this, Observer { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    viewModel.loadPendingJourneys()
                    viewModel.loadCurrentJourneys()
                    viewModel.loadUpcomingJourneys()
                    viewModel.loadPastJourneys()
                    Toast.makeText(context, "Declined!", Toast.LENGTH_LONG).show()
                }
                is Resource.Error -> {
                    Toast.makeText(context, response.error?.message, Toast.LENGTH_LONG).show()
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
                    result.data?.let { setUpPendingJourneyRV(it) }
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

    private fun observeNavigation() {
        viewModel.navigateToJourneyPlans.observe(this, Observer { result ->
            if (result != null) {
                val navController = findNavController()
                val actions = ProfileFragmentDirections.fromProfileToJourneyPlan(result)
                navController.navigate(actions)
            }
        })
    }

    private fun setUpPendingJourneyRV(journeyList: List<Journey>) {

        val adapter = context?.let {
            PendingJourneysRVAdapter(
                journeyList,
                viewModel
            )
        }
        binding.pendingJourneys.layoutManager = LinearLayoutManager(context)

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deletePendingInvitation(
                    adapter!!.getJourneyIdAtPosition(viewHolder.adapterPosition)
                )
            }
        }).attachToRecyclerView(binding.pendingJourneys)

        binding.pendingJourneys.adapter = adapter
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
