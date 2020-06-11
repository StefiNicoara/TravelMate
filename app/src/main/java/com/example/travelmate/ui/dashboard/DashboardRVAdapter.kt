package com.example.travelmate.ui.dashboard

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
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class DashboardRVAdapter(
    private val context: Context,
    var attractionsList: List<Attraction>,
    var viewModel: DashboardViewModel
) :
    RecyclerView.Adapter<DashboardRVAdapter.AttractionsViewHolder>() {

    private val subscriptions = CompositeDisposable()

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


    private fun handleFavoritesState(holder: AttractionsViewHolder, position: Int) {
        val observer = viewModel.isFavoriteByCurrentUser(attractionsList[position].id)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    holder.attractionInfoBinding.favoritesButton.isChecked = it
                }
            )
        subscriptions.add(observer)
    }

    private fun handleFavorites(holder: AttractionsViewHolder, position: Int) {
        holder.attractionInfoBinding.favoritesButton.setOnClickListener {
            if (holder.attractionInfoBinding.favoritesButton.isChecked) {
                viewModel.addToFavorites(attractionsList[position])
            } else {
                viewModel.removeFromFavorites(attractionsList[position])
            }
        }
    }

    private fun handleLikeState(holder: AttractionsViewHolder, position: Int) {
        val observer = viewModel.isLikedByCurrentUser(attractionsList[position].id)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    holder.attractionInfoBinding.likeButton.isChecked = it
                    if (it) {
                        holder.attractionInfoBinding.likesValue.setTextColor(Color.parseColor("#FF8B85"))
                    } else {
                        holder.attractionInfoBinding.likesValue.setTextColor(Color.parseColor("#585858"))
                    }
                }
            )
        subscriptions.add(observer)
    }


    private fun handleLikes(holder: AttractionsViewHolder, position: Int) {
        holder.attractionInfoBinding.likeButton.setOnClickListener {
            if (holder.attractionInfoBinding.likeButton.isChecked) {
                viewModel.addLike(attractionsList[position].id)
                holder.attractionInfoBinding.likesValue.setTextColor(Color.parseColor("#FF8B85"))
                attractionsList[position].likes++
                holder.attractionInfoBinding.likesValue.text =
                    attractionsList[position].likes.toString()
            } else {
                viewModel.undoLike(attractionsList[position].id)
                holder.attractionInfoBinding.likesValue.setTextColor(Color.parseColor("#585858"))
                attractionsList[position].likes--
                holder.attractionInfoBinding.likesValue.text =
                    attractionsList[position].likes.toString()
            }
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
        handleLikeState(holder, position)
        handleLikes(holder, position)
        handleFavoritesState(holder, position)
        handleFavorites(holder, position)
        handleNavigation(holder, position)
    }

    class AttractionsViewHolder(itemBinding: LayoutAttractionCellBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var attractionInfoBinding = itemBinding
    }
}
