package com.example.travelmate.ui.addAttraction.mapSearch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentMapSearchBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import android.location.Geocoder
import android.location.Address
import android.widget.Toast
import java.io.IOException
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.travelmate.utils.LoadingFragment
import com.example.travelmate.utils.NEW_DIALOG
import com.example.travelmate.utils.Resource
import org.koin.android.ext.android.inject

class MapSearchFragment : Fragment(), OnMapReadyCallback {

    private val viewModel by inject<MapSearchViewModel>()
    private lateinit var binding: FragmentMapSearchBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var loadingFragment: LoadingFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_map_search, container, false)
        loadingFragment = LoadingFragment()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myMap = binding.mapView
        binding.foundPin = false
        myMap.onCreate(null)
        myMap.onResume()
        myMap.getMapAsync(this)
        handleSearchClick()
        observeAttractions()
    }

    override fun onMapReady(map: GoogleMap?) {
        MapsInitializer.initialize(context)
        if (map != null) {
            googleMap = map
        }
    }

    private fun handleSearchClick() {
        val searchText = binding.searchText.text
        binding.searchBtn.setOnClickListener {
            if (searchText.isNotEmpty()) {
                geoLocate(searchText.toString())
            } else {
                Toast.makeText(context, "Search field can not be empty!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun geoLocate(text: String) {
        val geoCoder = Geocoder(context)
        var list: List<Address> = ArrayList()
        try {
            list = geoCoder.getFromLocationName(text, 1)
        } catch (e: IOException) {
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
        }
        if (list.isNotEmpty()) {
            val address = list[0]
            moveCamera(LatLng(address.latitude, address.longitude), address.getAddressLine(0))
            binding.confirmBtn.setOnClickListener {
                viewModel.updateAttraction(arguments?.get("attractionId") as String, address)
            }
        }
    }

    private fun moveCamera(latLng: LatLng, title: String) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

        val options = MarkerOptions()
            .position(latLng)
            .title(title)
        googleMap.addMarker(options)
        hideSoftKeyboard()
        binding.foundPin = true
    }

    private fun observeAttractions() {
        viewModel.updateAttraction.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    loadingFragment.show(parentFragmentManager, NEW_DIALOG)
                }
                is Resource.Success -> {
                    loadingFragment.dismiss()
                    if (result.data != null) {
                        findNavController().navigate(R.id.navigation_dashboard)
                        Toast.makeText(context, "Published!", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Error -> {
                    loadingFragment.dismiss()
                    Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun hideSoftKeyboard() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

}
