package com.example.travelmate.ui.attractionDetails.viewOnMap

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

class ViewOnMapViewModel(private val attractionsRepository: AttractionsRepository) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    val attraction: LiveData<Resource<Attraction>> get() = mutableAttraction
    private var mutableAttraction: MutableLiveData<Resource<Attraction>> = MutableLiveData()


    fun getCurrentAttraction(attractionId: String) {
        val observer = attractionsRepository.getAttractionById(attractionId)
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
}