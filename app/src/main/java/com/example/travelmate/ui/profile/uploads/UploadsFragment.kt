package com.example.travelmate.ui.profile.uploads

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentUserAttractionsBinding
import com.example.travelmate.model.Attraction
import com.example.travelmate.ui.profile.ProfileFragmentDirections
import com.example.travelmate.utils.Resource
import org.koin.android.ext.android.inject


class UploadsFragment : Fragment() {


    private val viewModel by inject<UploadsViewModel>()
    private lateinit var binding: FragmentUserAttractionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_attractions, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.swipeLayout.setOnRefreshListener {
            observeFavoriteAttractions()
            binding.swipeLayout.isRefreshing = false
        }
        viewModel.loadFavoriteAttractions()
        observeFavoriteAttractions()
        observeNavigation()
    }

    private fun observeFavoriteAttractions() {
        viewModel.loadUploadedAttractions.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    result.data?.let { setUpRecyclerView(it) }
                    binding.recyclerView.adapter?.notifyDataSetChanged()

                }
                is Resource.Error -> {
                    Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun observeNavigation() {
        viewModel.detailsScreenNav.observe(this, Observer { attractionId ->
            if (attractionId != null) {
                val navController = findNavController()
                val actions = ProfileFragmentDirections.fromProfileToDetails(attractionId)
                navController.navigate(actions)
            }
        })
    }

    private fun setUpRecyclerView(attractionsList: List<Attraction>) {
        val adapter = context?.let {
            UploadsRVAdapter(
                it,
                attractionsList,
                viewModel
            )
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

}