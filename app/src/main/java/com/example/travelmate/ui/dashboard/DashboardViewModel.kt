package com.example.travelmate.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelmate.model.Attraction
import com.example.travelmate.model.AttractionTag
import com.example.travelmate.repository.AttractionsRepository
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class DashboardViewModel(private val repository: AttractionsRepository) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    val detailsScreenNav: LiveData<String> get() = mutableDetailsScreenNav
    private var mutableDetailsScreenNav: MutableLiveData<String> = MutableLiveData()

    val loadAttractions: LiveData<Resource<List<Attraction>>> get() = mutableLoadAttractions
    private var mutableLoadAttractions: MutableLiveData<Resource<List<Attraction>>> =
        MutableLiveData()

    fun filter(tags: List<AttractionTag>, s: String) {
        val observer = repository.filter(tags, s)
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
        return repository.likeAttractionTransaction(attractionId)
    }

    fun undoLike(attractionId: String) {
        repository.undoLikeAttractionTransaction(attractionId)
    }

    fun addToFavorites(attraction: Attraction) {
        repository.addAttractionToFavorites(attraction)
    }

    fun removeFromFavorites(attraction: Attraction) {
        repository.removeAttractionFromFavorites(attraction)
    }

    fun isLikedByCurrentUser(attractionId: String): Single<Boolean> {
        return repository.isLikedByCurrentUser(attractionId)
    }

    fun isFavoriteByCurrentUser(attractionId: String): Single<Boolean> {
        return repository.isFavoriteByCurrentUser(attractionId)
    }

    fun loadAttractionsTimeline() {
        mutableLoadAttractions.value = Resource.Loading()
        val observer = repository.loadAllAttractionsTimeline()
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

    fun loadAttractionsTrending() {
        mutableLoadAttractions.value = Resource.Loading()
        val observer = repository.loadAllAttractionsTrending()
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

    fun navigateToDetailsScreen(attractionId: String) {
        mutableDetailsScreenNav.postValue(attractionId)
    }
}