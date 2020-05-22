package com.example.travelmate.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentDashboardBinding
import com.example.travelmate.model.Attraction
import com.example.travelmate.utils.Resource
import com.example.travelmate.utils.recyclerViewAdapters.DashboardRVAdapter
import org.koin.android.ext.android.inject

class DashboardFragment : Fragment() {

    private val viewModel by inject<DashboardViewModel>()
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.loadAttractions()
        observeAttractions()
    }

    private fun observeAttractions() {
        viewModel.loadAttractions.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    result.data?.let { setUpDashboardRecyclerView(it) }
                    binding.attractionsRV.adapter?.notifyDataSetChanged()

                }
                is Resource.Error -> {
                    Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setUpDashboardRecyclerView(attractionsList: List<Attraction>) {
        val adapter = context?.let { DashboardRVAdapter(it, attractionsList) }
        binding.attractionsRV.layoutManager = LinearLayoutManager(context)
        binding.attractionsRV.adapter = adapter

    }
}
