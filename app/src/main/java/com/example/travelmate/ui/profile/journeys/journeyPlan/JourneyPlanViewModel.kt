package com.example.travelmate.ui.profile.journeys.journeyPlan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelmate.model.Journey
import com.example.travelmate.repository.JourneyRepository
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class JourneyPlanViewModel(private val repository: JourneyRepository) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    val loadJourney: LiveData<Resource<Journey>> get() = mutableJourney
    private var mutableJourney: MutableLiveData<Resource<Journey>> =
        MutableLiveData()


    fun loadJourney(journeyId: String) {
        val observer = repository.getJourneyById(journeyId)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutableJourney.postValue(it)
                },
                onError = {
                    mutableJourney.postValue(Resource.Error(AppError(message = it.message)))
                }
            )
        subscriptions.add(observer)
    }
}