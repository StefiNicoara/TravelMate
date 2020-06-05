package com.example.travelmate.ui.profile.journeys.journeyPlan

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.travelmate.R
import com.example.travelmate.databinding.LayoutJourneyPlanCellBinding
import com.example.travelmate.model.JourneyPlan
import com.squareup.picasso.Picasso
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.time.hours

class JourneyPlansRVAdapter(
    private val context: Context,
    var plansList: List<JourneyPlan>,
    var viewModel: JourneyPlanViewModel
) :
    RecyclerView.Adapter<JourneyPlansRVAdapter.JourneyPlansViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JourneyPlansViewHolder {
        val binding = DataBindingUtil.inflate<LayoutJourneyPlanCellBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_journey_plan_cell,
            parent,
            false
        )
        return JourneyPlansViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return plansList.size
    }

    fun getPlanAtPosition(position: Int): JourneyPlan {
        return plansList[position]
    }

    private fun loadPhoto(holder: JourneyPlansViewHolder, position: Int) {
        Picasso.with(context)
            .load(plansList[position].attraction.image)
            .placeholder(R.drawable.whiteimage)
            .fit()
            .centerCrop()
            .into(holder.planBinding.locationPhoto)
    }

    private fun loadLocation(holder: JourneyPlansViewHolder, position: Int) {
        holder.planBinding.location =
            plansList[position].attraction.city.name.capitalize() +
                    ", " +
                    plansList[position].attraction.city.country.capitalize()
    }

    override fun onBindViewHolder(holder: JourneyPlansViewHolder, position: Int) {
        holder.planBinding.attraction = plansList[position].attraction
        holder.planBinding.date = DateFormat.getDateInstance(DateFormat.DATE_FIELD)
            .format(plansList[position].date)


        val pattern = "h:mm a"
        val sdf = SimpleDateFormat(pattern, Locale.US)
        holder.planBinding.time = sdf.format(plansList[position].date)
        loadPhoto(holder, position)
        loadLocation(holder, position)
    }

    class JourneyPlansViewHolder(itemBinding: LayoutJourneyPlanCellBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var planBinding = itemBinding
    }
}
