package com.example.travelmate.utils.recyclerViewAdapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.travelmate.R
import com.example.travelmate.databinding.LayoutAttractionCellBinding
import com.example.travelmate.model.Attraction
import com.example.travelmate.ui.dashboard.DashboardFragment
import com.example.travelmate.ui.dashboard.DashboardViewModel
import com.squareup.picasso.Picasso

class DashboardRVAdapter(
    private val context: Context,
    var attractionsList: List<Attraction>,
    var viewModel: DashboardViewModel
) :
    RecyclerView.Adapter<DashboardRVAdapter.AttractionsViewHolder>() {

    private var triggered = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttractionsViewHolder {
        val binding = DataBindingUtil.inflate<LayoutAttractionCellBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_attraction_cell,
            parent,
            false
        )
        return AttractionsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return attractionsList.size
    }

    private fun handleFavorites(holder: AttractionsViewHolder, position: Int) {

        holder.attractionInfoBinding.favoritesButton.setOnClickListener {
            triggered = true
        }

        holder.attractionInfoBinding.favoritesButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && triggered) {
                viewModel.addToFavorites(attractionsList[position])
            } else {
                viewModel.removeFromFavorites(attractionsList[position])
            }
        }
    }


    private fun handleLikes(holder: AttractionsViewHolder, position: Int) {
        holder.attractionInfoBinding.likeButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.addLike(attractionsList[position].id)
                displayLikeValue(holder, 1)
            } else {
                viewModel.undoLike(attractionsList[position].id)
                displayLikeValue(holder, -1)
            }
        }
    }

    private fun displayLikeValue(holder: AttractionsViewHolder, addedValue: Int) {
        val currentLikes = holder.attractionInfoBinding.likesValue.text.toString()
        val newLikes = currentLikes.toIntOrNull()?.plus(addedValue)
        holder.attractionInfoBinding.likesValue.text = newLikes.toString()


        if (addedValue == 1) {
            holder.attractionInfoBinding.likesValue.setTextColor(Color.parseColor("#FF8B85"))
        } else {
            holder.attractionInfoBinding.likesValue.setTextColor(Color.parseColor("#585858"))
        }
    }


    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: AttractionsViewHolder, position: Int) {
        val currentAttraction = attractionsList[position]
        holder.attractionInfoBinding.attraction = currentAttraction
        holder.attractionInfoBinding.location =
            currentAttraction.city.name.capitalize() + ", " + currentAttraction.city.country.capitalize()
        handleLikes(holder, position)
        handleFavorites(holder, position)
        Picasso.with(context)
            .load(currentAttraction.image)
            .placeholder(R.drawable.whiteimage)
            .fit()
            .centerCrop()
            .into(holder.attractionInfoBinding.locationPhoto)
    }

    class AttractionsViewHolder(itemBinding: LayoutAttractionCellBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var attractionInfoBinding = itemBinding
    }
}
