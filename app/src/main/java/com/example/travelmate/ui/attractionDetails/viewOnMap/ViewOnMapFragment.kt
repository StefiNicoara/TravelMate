package com.example.travelmate.ui.attractionDetails.viewOnMap
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

import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentViewOnMapBinding
import com.example.travelmate.utils.Resource
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.android.ext.android.inject

class ViewOnMapFragment : Fragment(), OnMapReadyCallback {

    private val viewModel by inject<ViewOnMapViewModel>()
    private lateinit var binding: FragmentViewOnMapBinding
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_view_on_map, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController()
                val actions =
                    ViewOnMapFragmentDirections.fromViewOnMapToDetails(arguments?.get("attractionId") as String)
                navController.navigate(actions)
            }
        })

        val myMap = binding.mapView
        myMap.onCreate(null)
        myMap.onResume()
        myMap.getMapAsync(this)
        viewModel.getCurrentAttraction(arguments?.get("attractionId") as String)
        observeAttraction()

    }

    private fun observeAttraction() {
        viewModel.attraction.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    moveCamera(
                        LatLng(result.data?.latitude ?: 0.0, result.data?.longitude ?: 0.0),
                        result.data?.title ?: ""
                    )
                }
                is Resource.Error -> {
                    Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun moveCamera(latLng: LatLng, title: String) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

        val options = MarkerOptions()
            .position(latLng)
            .title(title)
        googleMap.addMarker(options)
    }

    override fun onMapReady(map: GoogleMap?) {
        MapsInitializer.initialize(context)
        if (map != null) {
            googleMap = map
        }
    }


}
