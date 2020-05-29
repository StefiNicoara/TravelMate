package com.example.travelmate.ui.profile.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentFavoritesBinding
import com.example.travelmate.model.Attraction
import com.example.travelmate.utils.Resource
import org.koin.android.ext.android.inject


class FavoritesFragment : Fragment() {

    private val viewModel by inject<FavoritesViewModel>()
    private lateinit var binding: FragmentFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
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
    }


    private fun observeFavoriteAttractions() {
        viewModel.loadFavoriteAttractions.observe(this, Observer { result ->
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


    private fun setUpRecyclerView(attractionsList: List<Attraction>) {
        val adapter = context?.let {
            FavoritesRVAdapter(
                it,
                attractionsList,
                viewModel
            )
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }
}
