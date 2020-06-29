package com.example.travelmate.ui.profile.journeys

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentAddJourneyBinding
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.travelmate.utils.*
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject
import java.text.DateFormat
import java.util.*


class AddJourneyFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentAddJourneyBinding
    private val viewModel by inject<AddJourneyViewModel>()
    private var pickerFlag: Boolean? = null
    private lateinit var loadingFragment: LoadingFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_journey, container, false)
        loadingFragment = LoadingFragment()
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
        binding.addPhoto.setOnClickListener {
            openFileChooser()
        }
        binding.addButton.setOnClickListener {
            viewModel.journeyName.set(binding.journeyName.text.toString())
            viewModel.createJourney()
        }
        publishAttraction()

    }

    private fun publishAttraction() {
        viewModel.createJourney.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    loadingFragment.show(parentFragmentManager, NEW_DIALOG)
                }
                is Resource.Success -> {
                    loadingFragment.dismiss()
                    Toast.makeText(context, "Created!", Toast.LENGTH_LONG).show()
                    val navController = findNavController()
                    navController.navigate(R.id.navigation_dashboard)
                }
                is Resource.Error -> {
                    loadingFragment.dismiss()
                    Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val date = c.time

        val currentDateString = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(date)

        when (pickerFlag) {
            true -> {
                viewModel.startDate = date
                binding.startDate.text = currentDateString
            }
            false -> {
                viewModel.endDate = date
                binding.endDate.text = currentDateString
            }
        }
    }


    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            viewModel.imageUri = data.data!!
            viewModel.imageExtension = getFileExtension(data.data!!)

            binding.addPhoto.background = null
            Picasso.with(context)
                .load(viewModel.imageUri)
                .fit()
                .centerCrop()
                .into(binding.addPhoto)
        }
    }

    private fun getFileExtension(uri: Uri): String {
        val cR: ContentResolver? = context?.contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR?.getType(uri)).toString()
    }
}