package com.example.travelmate.ui.profile.journeys.journeyPlan.viewJourneyOnMap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentViewOnMapBinding
import com.example.travelmate.model.JourneyPlan
import com.example.travelmate.ui.attractionDetails.viewOnMap.ViewOnMapFragmentDirections
import com.example.travelmate.utils.Resource
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.android.ext.android.inject

class ViewJourneyOnMapFragment : Fragment(), OnMapReadyCallback {

    private val viewModel by inject<ViewJourneyOnMapViewModel>()
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
                    ViewJourneyOnMapFragmentDirections.fromViewJourneyOnMapToJourneyPlan(arguments?.get("journeyId") as String)
                navController.navigate(actions)
            }
        })

        val myMap = binding.mapView
        myMap.onCreate(null)
        myMap.onResume()
        myMap.getMapAsync(this)
        viewModel.loadJourney(arguments?.get("journeyId") as String)
        observeJourney()

    }

    private fun observeJourney() {
        viewModel.loadJourney.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    setUpPins(result.data?.journeyPlans ?: listOf())
                }
                is Resource.Error -> {
                    Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setUpPins(list: List<JourneyPlan>) {
        if (list.isNotEmpty()) {
            moveCamera(
                LatLng(list[0].attraction.latitude ?: 0.0, list[0].attraction.longitude ?: 0.0)
            )

            list.forEach {
                createPin(
                    LatLng(it.attraction.latitude ?: 0.0, it.attraction.longitude ?: 0.0),
                    it.attraction.title
                )
            }
        } else {
            Toast.makeText(context, "There are no plans yet!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveCamera(latLng: LatLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    private fun createPin(latLng: LatLng, title: String) {
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
