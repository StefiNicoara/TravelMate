package com.example.travelmate.ui.profile.uploads

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelmate.model.Attraction
import com.example.travelmate.repository.AttractionsRepository
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class UploadsViewModel(private val repository: AttractionsRepository) : ViewModel() {
    private val subscriptions = CompositeDisposable()

    val loadUploadedAttractions: LiveData<Resource<List<Attraction>>> get() = mutableLoadAttractions
    private var mutableLoadAttractions: MutableLiveData<Resource<List<Attraction>>> =
        MutableLiveData()

    val detailsScreenNav: LiveData<String> get() = mutableDetailsScreenNav
    private var mutableDetailsScreenNav: MutableLiveData<String> = MutableLiveData()

    fun loadFavoriteAttractions() {
        val observer = repository.loadUploadedAttractions()
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

    fun addToFavorites(attraction: Attraction) {
        repository.addAttractionToFavorites(attraction)
    }

    fun removeFromFavorites(attraction: Attraction) {
        repository.removeAttractionFromFavorites(attraction)
        loadFavoriteAttractions()
    }

    fun navigateToDetailsScreen(attractionId: String) {
        mutableDetailsScreenNav.postValue(attractionId)
    }

    fun isLikedByCurrentUser(attractionId: String): Single<Boolean> {
        return repository.isLikedByCurrentUser(attractionId)
    }

    fun isFavoriteByCurrentUser(attractionId: String): Single<Boolean> {
        return repository.isFavoriteByCurrentUser(attractionId)
    }

}