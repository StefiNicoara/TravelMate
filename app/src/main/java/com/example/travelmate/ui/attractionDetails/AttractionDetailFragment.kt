package com.example.travelmate.ui.attractionDetails

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
import androidx.navigation.fragment.navArgs

import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentAttractionDetailBinding
import com.example.travelmate.utils.Resource
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject

class AttractionDetailFragment : Fragment() {

    private val viewModel by inject<AttractionDetailViewModel>()
    private lateinit var binding: FragmentAttractionDetailBinding
    private var isCheckedFavorites = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_attraction_detail, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController()
                navController.navigate(R.id.navigation_dashboard)
            }
        })
        viewModel.getCurrentAttraction(arguments?.get("attractionId") as String)
        observeAttractions()
        handleFavorites(arguments?.get("attractionId") as String)
    }

    private fun observeAttractions() {
        viewModel.attraction.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    binding.attraction = result.data
                    loadLocation(result.data?.city?.name, result.data?.city?.country)
                    result.data?.image?.let { setPhoto(it) }
                }
                is Resource.Error -> {
                    Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun loadLocation(city: String?, country: String?) {
        binding.location = "$city, $country"
    }

    private fun setPhoto(img: String) {
        Picasso.with(context)
            .load(img)
            .fit()
            .centerCrop()
            .into(binding.image)
    }

    private fun handleFavorites(attractionId: String) {

        binding.favoritesButton.setOnClickListener {
            if (isCheckedFavorites) {
                viewModel.addToFavorites(attractionId)
            } else {
                viewModel.removeFromFavorites(attractionId)
            }
        }

        binding.favoritesButton.setOnCheckedChangeListener { _, isChecked ->
            isCheckedFavorites = isChecked
        }
    }
}
