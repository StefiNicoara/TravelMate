package com.example.travelmate.ui.profile.journeys

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentAddJourneyBinding
import android.widget.DatePicker
import com.example.travelmate.utils.DATE_PICKER
import java.text.DateFormat
import java.util.*


class AddJourneyFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentAddJourneyBinding
    var pickerFlag: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_journey, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.startDate.setOnClickListener {
            pickerFlag = true
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.show(childFragmentManager, DATE_PICKER)
        }

        binding.endDate.setOnClickListener {
            pickerFlag = false
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.show(childFragmentManager, DATE_PICKER)
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val currentDateString = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.time)

        when (pickerFlag) {
            true -> {
                binding.startDate.text = currentDateString
            }
            false -> {
                binding.endDate.text = currentDateString
            }
        }
    }
}