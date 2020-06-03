package com.example.travelmate.ui.createJourneyPlan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.travelmate.R
import com.example.travelmate.databinding.LayoutJourneyCellBinding
import com.example.travelmate.model.Journey
import com.squareup.picasso.Picasso
import java.text.DateFormat

class ChooseJourneyRVAdapter(
    private val context: Context,
    var journeyList: List<Journey>,
    var viewModel: CreateJourneyPlanViewModel
) :
    RecyclerView.Adapter<ChooseJourneyRVAdapter.JourneysViewHolder>() {

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

    private fun handleClick(holder: JourneysViewHolder, position: Int) {
        holder.journeyInfoBinding.cardViewId.setOnClickListener {
            holder.journeyInfoBinding.gradient.alpha = 0.5f
            viewModel.journeyId = journeyList[position].journeyId
        }
    }

    override fun onBindViewHolder(holder: JourneysViewHolder, position: Int) {
        holder.journeyInfoBinding.journey = journeyList[position]
        holder.journeyInfoBinding.startDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD)
            .format(journeyList[position].startDate)
        holder.journeyInfoBinding.endDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD)
            .format(journeyList[position].endDate)
        holder.journeyInfoBinding.shareJourney.visibility = View.GONE
        holder.journeyInfoBinding.isUpcoming = false
        loadPhoto(holder, position)
        handleClick(holder, position)
    }

    class JourneysViewHolder(itemBinding: LayoutJourneyCellBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var journeyInfoBinding = itemBinding
    }
}
