package com.example.travelmate.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentDashboardBinding
import com.example.travelmate.model.Attraction
import com.example.travelmate.model.AttractionTag
import com.example.travelmate.utils.LoadingFragment
import com.example.travelmate.utils.NEW_DIALOG
import com.example.travelmate.utils.Resource
import org.koin.android.ext.android.inject

class DashboardFragment : Fragment() {

    private val viewModel by inject<DashboardViewModel>()
    private lateinit var binding: FragmentDashboardBinding
    private var tags: MutableList<AttractionTag> = mutableListOf()
    var searchTerm: String = ""
    private lateinit var loadingFragment : LoadingFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        loadingFragment = LoadingFragment()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.loadAttractionsTimeline()
        handleTags()
        handleSearch()
        handleFilter()
        observeAttractions()
        observeNavigation()
    }

    private fun observeNavigation() {
        viewModel.detailsScreenNav.observe(this, Observer { attractionId ->
            if (attractionId != null) {
                val navController = findNavController()
                val actions = DashboardFragmentDirections.fromDashboardToDetails(attractionId)
                navController.navigate(actions)
            }
        })
    }

    private fun observeAttractions() {
        viewModel.loadAttractions.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    loadingFragment.show(parentFragmentManager, NEW_DIALOG)
                }
                is Resource.Success -> {
                    loadingFragment.dismiss()
                    result.data?.let { setUpDashboardRecyclerView(it) }
                    binding.attractionsRV.adapter?.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    loadingFragment.dismiss()
                    Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setUpDashboardRecyclerView(attractionsList: List<Attraction>) {
        val adapter = context?.let {
            DashboardRVAdapter(
                it,
                attractionsList,
                viewModel
            )
        }
        binding.attractionsRV.layoutManager = LinearLayoutManager(context)
        binding.attractionsRV.adapter = adapter
    }

    private fun handleFilter() {
        binding.timeline.setOnClickListener {
            viewModel.loadAttractionsTimeline()
        }
        binding.trending.setOnClickListener {
            viewModel.loadAttractionsTrending()
        }
    }


    private fun handleSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(s: String): Boolean {
                searchTerm = s
                viewModel.filter(tags, searchTerm)
                return true
            }

            override fun onQueryTextSubmit(s: String): Boolean {
                searchTerm = s
                viewModel.filter(tags, searchTerm)
                return true
            }
        })

        binding.searchView.setOnCloseListener {
            searchTerm = ""
            viewModel.loadAttractionsTimeline()
            true
        }
    }


    private fun handleTags() {
        binding.social.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tags.add(AttractionTag.SOCIAL)
            } else {
                tags.remove(AttractionTag.SOCIAL)
            }
            viewModel.filter(tags, searchTerm)
        }

        binding.cultural.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tags.add(AttractionTag.CULTURAL)
            } else {
                tags.remove(AttractionTag.CULTURAL)
            }
            viewModel.filter(tags, searchTerm)
        }

        binding.recreational.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tags.add(AttractionTag.RECREATIONAL)
            } else {
                tags.remove(AttractionTag.RECREATIONAL)
            }
            viewModel.filter(tags, searchTerm)
        }

        binding.`fun`.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tags.add(AttractionTag.FUN)
            } else {
                tags.remove(AttractionTag.FUN)
            }
            viewModel.filter(tags, searchTerm)
        }
        binding.food.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tags.add(AttractionTag.FOOD)
            } else {
                tags.remove(AttractionTag.FOOD)
            }
            viewModel.filter(tags, searchTerm)
        }
        binding.cafe.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tags.add(AttractionTag.CAFE)
            } else {
                tags.remove(AttractionTag.CAFE)
            }
            viewModel.filter(tags, searchTerm)
        }
        binding.bar.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tags.add(AttractionTag.BAR)
            } else {
                tags.remove(AttractionTag.BAR)
            }
            viewModel.filter(tags, searchTerm)
        }

        binding.accommodation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tags.add(AttractionTag.ACCOMODATION)
            } else {
                tags.remove(AttractionTag.ACCOMODATION)
            }
            viewModel.filter(tags, searchTerm)
        }
    }
}
