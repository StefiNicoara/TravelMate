package com.example.travelmate.ui.profile.journeys.journeyPlan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentJourneyPlanBinding
import com.example.travelmate.model.Journey
import com.example.travelmate.model.JourneyPlan
import com.example.travelmate.utils.Resource
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject
import java.text.DateFormat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper


class JourneyPlanFragment : Fragment() {

    private lateinit var binding: FragmentJourneyPlanBinding
    private val viewModel by inject<JourneyPlanViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_journey_plan, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController()
                navController.navigate(R.id.navigation_profile)
            }
        })

        binding.hasPlans = false
        viewModel.loadJourney(arguments?.get("journeyId") as String)
        observeJourney()
        observeDelete()
    }

    private fun observeJourney() {
        viewModel.loadJourney.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    setUpBinding(result.data)
                }
                is Resource.Error -> {
                    Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun observeDelete() {
        viewModel.deleteResponse.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    viewModel.loadJourney(arguments?.get("journeyId") as String)
                    Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show()
                }
                is Resource.Error -> {
                    Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }


    private fun setJourneyPlansRV(plansList: List<JourneyPlan>) {

        val list = plansList.sortedBy { jp ->
            jp.date
        }

        val adapter = context?.let {
            JourneyPlansRVAdapter(it, list, viewModel)
        }
        binding.plansRV.layoutManager = LinearLayoutManager(context)

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
                viewModel.deletePlan(
                    adapter!!.getPlanAtPosition(viewHolder.adapterPosition),
                    arguments?.get("journeyId") as String
                )
            }
        }).attachToRecyclerView(binding.plansRV)
        binding.plansRV.adapter = adapter
    }

    private fun setPhoto(img: String) {
        Picasso.with(context)
            .load(img)
            .fit()
            .centerCrop()
            .into(binding.journeyImage)
    }

    private fun setUpBinding(journey: Journey?) {
        journey?.image?.let { setPhoto(it) }
        binding.name = journey?.name
        binding.startDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD)
            .format(journey!!.startDate)
        binding.endDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD)
            .format(journey.endDate)
        if (journey.journeyPlans != null) {
            binding.hasPlans = true
            setJourneyPlansRV(journey.journeyPlans!!)
            binding.plansRV.adapter?.notifyDataSetChanged()
        } else {
            binding.hasPlans = false
        }
    }

}
