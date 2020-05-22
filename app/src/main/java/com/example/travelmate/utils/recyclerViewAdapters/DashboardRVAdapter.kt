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

class DashboardRVAdapter(
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
    }

    class AttractionsViewHolder(itemBinding: LayoutAttractionCellBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var attractionInfoBinding = itemBinding
    }
}
//
//<ImageView
//android:layout_width="20dp"
//android:layout_height="20dp"
//android:layout_gravity="bottom"
//android:layout_marginStart="@dimen/xsmall_margin_padding"
//android:background="@drawable/ic_bar"
//android:contentDescription="@null" />