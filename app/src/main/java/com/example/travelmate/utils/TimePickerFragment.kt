package com.example.travelmate.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.text.DateFormat
import java.util.*

class TimePickerFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return TimePickerDialog(
            context!!,
            parentFragment as TimePickerDialog.OnTimeSetListener,
            hour,
            minute,
            true
        )
    }
}