package com.example.travelmate.ui.profile.journeys

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

class JourneysViewModel(private val repository: JourneyRepository) : ViewModel() {
    private val subscriptions = CompositeDisposable()

    val loadPendingJourneys: LiveData<Resource<List<Journey>>> get() = mutablePendingJourneys
    private var mutablePendingJourneys: MutableLiveData<Resource<List<Journey>>> =
        MutableLiveData()

    val loadCurrentJourneys: LiveData<Resource<List<Journey>>> get() = mutableCurrentJourneys
    private var mutableCurrentJourneys: MutableLiveData<Resource<List<Journey>>> =
        MutableLiveData()

    val loadUpcomingJourneys: LiveData<Resource<List<Journey>>> get() = mutableUpcomingJourneys
    private var mutableUpcomingJourneys: MutableLiveData<Resource<List<Journey>>> =
        MutableLiveData()

    val loadPastJourneys: LiveData<Resource<List<Journey>>> get() = mutablePastJourneys
    private var mutablePastJourneys: MutableLiveData<Resource<List<Journey>>> =
        MutableLiveData()

    val startJourney: LiveData<Resource<Boolean>> get() = mutableStartJourney
    private var mutableStartJourney: MutableLiveData<Resource<Boolean>> = MutableLiveData()

    fun loadPendingJourneys() {
        val observer = repository.getPendingJourneys()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutablePendingJourneys.postValue(it)
                },
                onError = {
                    mutablePendingJourneys.postValue(Resource.Error(AppError(message = it.message)))
                }
            )
        subscriptions.add(observer)
    }

    fun loadCurrentJourneys() {
        val observer = repository.getCurrentJourneys()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutableCurrentJourneys.postValue(it)
                },
                onError = {
                    mutableCurrentJourneys.postValue(Resource.Error(AppError(message = it.message)))
                }
            )
        subscriptions.add(observer)
    }

    fun loadUpcomingJourneys() {
        val observer = repository.getUpcomingJourneys()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutableUpcomingJourneys.postValue(it)
                },
                onError = {
                    mutableUpcomingJourneys.postValue(Resource.Error(AppError(message = it.message)))
                }
            )
        subscriptions.add(observer)
    }

    fun loadPastJourneys() {
        val observer = repository.getPastJourneys()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutablePastJourneys.postValue(it)
                },
                onError = {
                    mutablePastJourneys.postValue(Resource.Error(AppError(message = it.message)))
                }
            )
        subscriptions.add(observer)
    }

    fun startJourney(journeyId: String) {
        val observer = repository.startJourney(journeyId)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutableStartJourney.postValue(it)
                },
                onError = {
                    mutableStartJourney.postValue(Resource.Error(AppError(message = it.message)))
                }
            )
        subscriptions.add(observer)
    }
}