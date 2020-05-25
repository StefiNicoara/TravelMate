package com.example.travelmate.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelmate.model.Attraction
import com.example.travelmate.model.AttractionTag
import com.example.travelmate.repository.AttractionsRepository
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class DashboardViewModel(private val repository: AttractionsRepository) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    val newLikesValue: LiveData<Long> get() = mutableLikeValue
    private var mutableLikeValue: MutableLiveData<Long> =
        MutableLiveData()

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
        val observer = repository.likeAttractionTransaction(attractionId)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutableLikeValue.postValue(it)
                }
            )
        subscriptions.add(observer)
    }

    fun undoLike(attractionId: String) {
        val observer = repository.undoLikeAttractionTransaction(attractionId)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutableLikeValue.postValue(it)
                }
            )
        subscriptions.add(observer)
    }

    fun addToFavorites(attraction: Attraction) {
        repository.addAttractionToFavorites(attraction)
    }

    fun removeFromFavorites(attraction: Attraction) {
       repository.removeAttractionFromFavorites(attraction)
    }

    fun loadAttractions() {
        val observer = repository.loadAllAttractions()
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
}