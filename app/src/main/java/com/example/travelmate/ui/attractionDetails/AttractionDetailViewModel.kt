package com.example.travelmate.ui.attractionDetails

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

class AttractionDetailViewModel(private val repository: AttractionsRepository) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    val attraction: LiveData<Resource<Attraction>> get() = mutableAttraction
    private var mutableAttraction: MutableLiveData<Resource<Attraction>> = MutableLiveData()


    fun getCurrentAttraction(attractionId: String) {
        val observer = repository.getAttractionById(attractionId)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutableAttraction.postValue(it)
                },
                onError = {
                    mutableAttraction.postValue(Resource.Error(AppError(message = it.message)))
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


}
