package com.example.travelmate.ui.createJourneyPlan

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentCreateJourneyPlanBinding
import com.example.travelmate.model.Journey
import com.example.travelmate.utils.*
import org.koin.android.ext.android.inject
import java.text.DateFormat
import java.util.*

class CreateJourneyPlanFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private val viewModel by inject<CreateJourneyPlanViewModel>()
    private lateinit var binding: FragmentCreateJourneyPlanBinding
    private lateinit var loadingFragment: LoadingFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_create_journey_plan,
                container,
                false
            )
        loadingFragment = LoadingFragment()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController()
                val actions =
                    CreateJourneyPlanFragmentDirections.fromCreatePlanToDetails(arguments?.get("attractionId") as String)
                navController.navigate(actions)
            }
        })

        binding.date.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.show(childFragmentManager, DATE_PICKER)
        }
        binding.time.setOnClickListener {
            val timePickerFragment = TimePickerFragment()
            timePickerFragment.show(childFragmentManager, TIME_PICKER)
        }
        binding.addButton.setOnClickListener {
            viewModel.addAttractionToPlan()
        }

        viewModel.loadJourneys()
        viewModel.getCurrentAttraction(arguments?.get("attractionId") as String)
        observePastJourneys()
        observeAddResponse()
    }

    private fun observeAddResponse() {
        viewModel.attractionAdded.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    loadingFragment.show(parentFragmentManager, NEW_DIALOG)
                }
                is Resource.Success -> {
                    loadingFragment.dismiss()
                    Toast.makeText(context, "Added!", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.navigation_dashboard)

                }
                is Resource.Error -> {
                    loadingFragment.dismiss()
                    Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun observePastJourneys() {
        viewModel.loadJourneys.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    loadingFragment.show(parentFragmentManager, NEW_DIALOG)
                }
                is Resource.Success -> {
                    loadingFragment.dismiss()
                    result.data?.let { setJourneysRV(it) }
                    binding.journeysRV.adapter?.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    loadingFragment.dismiss()
                    Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setJourneysRV(journeyList: List<Journey>) {
        val adapter = context?.let {
            ChooseJourneyRVAdapter(
                it,
                journeyList,
                viewModel
            )
        }
        binding.journeysRV.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.journeysRV.adapter = adapter
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        viewModel.calendar?.set(Calendar.YEAR, year)
        viewModel.calendar?.set(Calendar.MONTH, month)
        viewModel.calendar?.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val date = viewModel.calendar!!.time
        val currentDateString = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(date)
        binding.date.text = currentDateString

    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        viewModel.calendar?.set(Calendar.HOUR_OF_DAY, hourOfDay)
        viewModel.calendar?.set(Calendar.MINUTE, minute)
        binding.time.text = "$hourOfDay : $minute"
    }
}
