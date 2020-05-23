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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentDashboardBinding
import com.example.travelmate.model.Attraction
import com.example.travelmate.model.AttractionTag
import com.example.travelmate.utils.Resource
import com.example.travelmate.utils.recyclerViewAdapters.DashboardRVAdapter
import org.koin.android.ext.android.inject

class DashboardFragment : Fragment() {

    private val viewModel by inject<DashboardViewModel>()
    private lateinit var binding: FragmentDashboardBinding
    var tags: MutableList<AttractionTag> = mutableListOf()

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
        handleTags()
        handleSearch()
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

    private fun handleSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(s: String): Boolean {
                viewModel.handleSearchTerm(s)
                return true
            }

            override fun onQueryTextSubmit(s: String): Boolean {
                viewModel.handleSearchTerm(s)
                return true
            }
        })

        binding.searchView.setOnCloseListener {
            viewModel.loadAttractions()
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
            viewModel.handleTagsFilter(tags)
        }

        binding.cultural.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tags.add(AttractionTag.CULTURAL)
            } else {
                tags.remove(AttractionTag.CULTURAL)
            }
            viewModel.handleTagsFilter(tags)
        }

        binding.recreational.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tags.add(AttractionTag.RECREATIONAL)
            } else {
                tags.remove(AttractionTag.RECREATIONAL)
            }
            viewModel.handleTagsFilter(tags)
        }

        binding.`fun`.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tags.add(AttractionTag.FUN)
            } else {
                tags.remove(AttractionTag.FUN)
            }
            viewModel.handleTagsFilter(tags)
        }
        binding.food.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tags.add(AttractionTag.FOOD)
            } else {
                tags.remove(AttractionTag.FOOD)
            }
            viewModel.handleTagsFilter(tags)
        }
        binding.cafe.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tags.add(AttractionTag.CAFE)
            } else {
                tags.remove(AttractionTag.CAFE)
            }
            viewModel.handleTagsFilter(tags)
        }
        binding.bar.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tags.add(AttractionTag.BAR)
            } else {
                tags.remove(AttractionTag.BAR)
            }
            viewModel.handleTagsFilter(tags)
        }

        binding.accommodation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tags.add(AttractionTag.ACCOMODATION)
            } else {
                tags.remove(AttractionTag.ACCOMODATION)
            }
            viewModel.handleTagsFilter(tags)
        }
    }
}
