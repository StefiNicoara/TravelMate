package com.example.travelmate.ui.profile.uploads

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.travelmate.R
import com.example.travelmate.databinding.LayoutAttractionCellBinding
import com.example.travelmate.model.Attraction
import com.example.travelmate.ui.profile.favorites.FavoritesViewModel
import com.squareup.picasso.Picasso

class UploadsRVAdapter(
    private val context: Context,
    var attractionsList: List<Attraction>,
    var viewModel: UploadsViewModel
) :
    RecyclerView.Adapter<UploadsRVAdapter.AttractionsViewHolder>() {

    private var isCheckedFavorites = false
    private var isCheckedLikes = false

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
            if (isCheckedFavorites) {
                viewModel.addToFavorites(attractionsList[position])
            } else {
                viewModel.removeFromFavorites(attractionsList[position])
            }
        }

        holder.attractionInfoBinding.favoritesButton.setOnCheckedChangeListener { _, isChecked ->
            isCheckedFavorites = isChecked
        }
    }


    private fun handleLikes(holder: AttractionsViewHolder, position: Int) {
        holder.attractionInfoBinding.likeButton.setOnClickListener {
            if (isCheckedLikes) {
                viewModel.addLike(attractionsList[position].id)
                displayLikeValue(holder, 1)
            } else {
                viewModel.undoLike(attractionsList[position].id)
                displayLikeValue(holder, -1)
            }
        }

        holder.attractionInfoBinding.likeButton.setOnCheckedChangeListener { _, isChecked ->
            isCheckedLikes = isChecked
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

    private fun handleNavigation(holder: AttractionsViewHolder, position: Int) {
        holder.attractionInfoBinding.cardViewId.setOnClickListener {
            viewModel.navigateToDetailsScreen(attractionsList[position].id)
        }
    }

    private fun loadPhoto(holder: AttractionsViewHolder, position: Int) {
        Picasso.with(context)
            .load(attractionsList[position].image)
            .placeholder(R.drawable.whiteimage)
            .fit()
            .centerCrop()
            .into(holder.attractionInfoBinding.locationPhoto)
    }

    private fun loadLocation(holder: AttractionsViewHolder, position: Int) {
        holder.attractionInfoBinding.location =
            attractionsList[position].city.name.capitalize() +
                    ", " +
                    attractionsList[position].city.country.capitalize()
    }


    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: AttractionsViewHolder, position: Int) {
        holder.attractionInfoBinding.attraction = attractionsList[position]
        loadLocation(holder, position)
        loadPhoto(holder, position)
        handleLikes(holder, position)
        handleFavorites(holder, position)
        handleNavigation(holder, position)
    }

    class AttractionsViewHolder(itemBinding: LayoutAttractionCellBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var attractionInfoBinding = itemBinding
    }
}
