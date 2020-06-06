package com.example.travelmate.ui.addAttraction

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentAddAttractionBinding
import com.example.travelmate.model.AttractionTag
import com.example.travelmate.utils.PICK_IMAGE_REQUEST
import com.example.travelmate.utils.Resource
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.ConnectionResult

class AddAttractionFragment : Fragment() {

    private val viewModel by inject<AddAttractionViewModel>()
    private lateinit var binding: FragmentAddAttractionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_attraction, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.viewModel = viewModel
        addPhoto()
        handleTags()
        publishAttraction()
        addLocation()
    }

    private fun publishAttraction() {
        viewModel.publishAttraction.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (result.data != null) {
                        val navController = findNavController()
                        val actions =
                            AddAttractionFragmentDirections.fromAddAttractionToAddLocation(result.data)
                        navController.navigate(actions)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun addPhoto() {
        viewModel.chooseImageClick.observe(this, Observer { clicked ->
            if (clicked) {
                openFileChooser()
            }
        })
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            viewModel.imageUri = data.data!!
            viewModel.imageExtension = getFileExtension(data.data!!)

            binding.addedPhoto.background = null
            Picasso.with(context)
                .load(viewModel.imageUri)
                .fit()
                .centerCrop()
                .into(binding.addedPhoto)
        }
    }

    private fun getFileExtension(uri: Uri): String {
        val cR: ContentResolver? = context?.contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR?.getType(uri)).toString()
    }

    private fun addLocation() {
        binding.addMapLocation.setOnClickListener {
            if (isServicesOK()) {
                viewModel.publishAttraction()
            }
        }
    }

    private fun isServicesOK(): Boolean {
        val available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
        when {
            available == ConnectionResult.SUCCESS -> {
                return true
            }
            GoogleApiAvailability.getInstance().isUserResolvableError(available) -> {
                val dialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(activity, available, 1)
                dialog.show()
            }
            else -> Toast.makeText(
                context,
                "You can't make map requests",
                Toast.LENGTH_SHORT
            ).show()
        }
        return false
    }

    private fun handleTags() {
        binding.social.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.tags.add(AttractionTag.SOCIAL)
            } else {
                viewModel.tags.remove(AttractionTag.SOCIAL)
            }
        }

        binding.cultural.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.tags.add(AttractionTag.CULTURAL)
            } else {
                viewModel.tags.remove(AttractionTag.CULTURAL)
            }
        }

        binding.recreational.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.tags.add(AttractionTag.RECREATIONAL)
            } else {
                viewModel.tags.remove(AttractionTag.RECREATIONAL)
            }
        }

        binding.`fun`.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.tags.add(AttractionTag.FUN)
            } else {
                viewModel.tags.remove(AttractionTag.FUN)
            }
        }
        binding.food.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.tags.add(AttractionTag.FOOD)
            } else {
                viewModel.tags.remove(AttractionTag.FOOD)
            }
        }
        binding.cafe.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.tags.add(AttractionTag.CAFE)
            } else {
                viewModel.tags.remove(AttractionTag.CAFE)
            }
        }
        binding.bar.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.tags.add(AttractionTag.BAR)
            } else {
                viewModel.tags.remove(AttractionTag.BAR)
            }
        }

        binding.accommodation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.tags.add(AttractionTag.ACCOMODATION)
            } else {
                viewModel.tags.remove(AttractionTag.ACCOMODATION)
            }
        }
    }

}
