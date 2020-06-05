package com.example.travelmate.ui.profile.journeys.recyclerViewAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.travelmate.R
import com.example.travelmate.databinding.LayoutPendingJourneyCellBinding
import com.example.travelmate.model.Journey
import com.example.travelmate.ui.profile.journeys.JourneysViewModel


class PendingJourneysRVAdapter(
    var journeyList: List<Journey>,
    var viewModel: JourneysViewModel
) :
    RecyclerView.Adapter<PendingJourneysRVAdapter.PendingJourneysViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingJourneysViewHolder {
        val binding = DataBindingUtil.inflate<LayoutPendingJourneyCellBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_pending_journey_cell,
            parent,
            false
        )
        return PendingJourneysViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return journeyList.size
    }

    fun getJourneyIdAtPosition(position: Int): String {
        return journeyList[position].journeyId
    }

    private fun acceptClick(holder: PendingJourneysViewHolder, position: Int) {
        holder.journeyInfoBinding.acceptBtn.setOnClickListener {
            viewModel.acceptJourney(journeyList[position].journeyId)
        }
    }

    override fun onBindViewHolder(holder: PendingJourneysViewHolder, position: Int) {
        holder.journeyInfoBinding.journeyName = journeyList[position].name
        acceptClick(holder, position)
    }

    class PendingJourneysViewHolder(itemBinding: LayoutPendingJourneyCellBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var journeyInfoBinding = itemBinding
    }
}
