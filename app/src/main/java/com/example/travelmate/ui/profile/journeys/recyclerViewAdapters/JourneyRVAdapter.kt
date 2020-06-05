package com.example.travelmate.ui.profile.journeys.recyclerViewAdapters

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.marginStart
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.travelmate.R
import com.example.travelmate.databinding.LayoutJourneyCellBinding
import com.example.travelmate.model.Journey
import com.example.travelmate.ui.profile.journeys.JourneysViewModel
import com.squareup.picasso.Picasso
import java.text.DateFormat
import kotlin.math.roundToInt

class JourneyRVAdapter(
    private val context: Context,
    var journeyList: List<Journey>,
    var viewModel: JourneysViewModel,
    var isUpcoming: Boolean
) :
    RecyclerView.Adapter<JourneyRVAdapter.JourneysViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JourneysViewHolder {
        val binding = DataBindingUtil.inflate<LayoutJourneyCellBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_journey_cell,
            parent,
            false
        )
        return JourneysViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return journeyList.size
    }

    private fun loadPhoto(holder: JourneysViewHolder, position: Int) {
        Picasso.with(context)
            .load(journeyList[position].image)
            .placeholder(R.drawable.whiteimage)
            .fit()
            .centerCrop()
            .into(holder.journeyInfoBinding.journeyImage)
    }

    private fun shareJourneyClick(holder: JourneysViewHolder, position: Int) {
        holder.journeyInfoBinding.shareJourney.setOnClickListener {
            viewModel.shareJourney(journeyList[position].journeyId)
        }
    }

    private fun startJourneyClick(holder: JourneysViewHolder, position: Int) {
        holder.journeyInfoBinding.markStart.setOnClickListener {
            viewModel.startJourney(journeyList[position].journeyId)
        }
    }

    private fun cardClick(holder: JourneysViewHolder, position: Int) {
        holder.journeyInfoBinding.cardViewId.setOnClickListener {
            viewModel.handleNavigationToPlans(journeyList[position].journeyId)
        }
    }

    override fun onBindViewHolder(holder: JourneysViewHolder, position: Int) {
        holder.journeyInfoBinding.journey = journeyList[position]
        holder.journeyInfoBinding.startDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD)
            .format(journeyList[position].startDate)
        holder.journeyInfoBinding.endDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD)
            .format(journeyList[position].endDate)
        holder.journeyInfoBinding.isUpcoming = isUpcoming
        loadPhoto(holder, position)
        startJourneyClick(holder, position)
        cardClick(holder, position)
        shareJourneyClick(holder, position)
    }

    class JourneysViewHolder(itemBinding: LayoutJourneyCellBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var journeyInfoBinding = itemBinding
    }
}
