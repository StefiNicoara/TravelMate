package com.example.travelmate.utils.recyclerViewAdapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.travelmate.R
import com.example.travelmate.databinding.LayoutAttractionCellBinding
import com.example.travelmate.model.Attraction
import com.squareup.picasso.Picasso

class DashboardRVAdapter(
    private val context: Context,
    var attractionsList: List<Attraction>
) :
    RecyclerView.Adapter<DashboardRVAdapter.AttractionsViewHolder>() {

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

    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: AttractionsViewHolder, position: Int) {
        val currentAttraction = attractionsList[position]
        holder.attractionInfoBinding.attraction = currentAttraction
        holder.attractionInfoBinding.location =
            currentAttraction.city.name.capitalize() + ", " + currentAttraction.city.country.capitalize()
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
