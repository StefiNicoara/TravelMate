package com.example.travelmate.ui.profile.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelmate.model.Attraction
import com.example.travelmate.repository.AttractionsRepository
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class FavoritesViewModel(private val repository: AttractionsRepository) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    val loadFavoriteAttractions: LiveData<Resource<List<Attraction>>> get() = mutableLoadAttractions
    private var mutableLoadAttractions: MutableLiveData<Resource<List<Attraction>>> = MutableLiveData()

    val detailsScreenNav: LiveData<String> get() = mutableDetailsScreenNav
    private var mutableDetailsScreenNav: MutableLiveData<String> = MutableLiveData()

    fun loadFavoriteAttractions() {
        val observer = repository.loadFavoriteAttractions()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutableLoadAttractions.postValue(it)
                },
                onError = {
                    mutableLoadAttractions.postValue(Resource.Error(AppError(message = it.message)))
                }
            )
        subscriptions.add(observer)
    }

    fun addLike(attractionId: String) {
        repository.likeAttractionTransaction(attractionId)
    }

    fun undoLike(attractionId: String) {
        repository.undoLikeAttractionTransaction(attractionId)
    }

    fun addToFavorites(attractionId: String) {
        repository.addAttractionToFavorites(attractionId)
    }

    fun removeFromFavorites(attractionId: String) {
        repository.removeAttractionFromFavorites(attractionId)
    }

    fun navigateToDetailsScreen(attractionId: String) {
        mutableDetailsScreenNav.postValue(attractionId)
    }

}