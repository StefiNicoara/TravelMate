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

    val shareJourney: LiveData<String> get() = mutableShareJourney
    private var mutableShareJourney: MutableLiveData<String> = MutableLiveData()

    val acceptJourney: LiveData<Resource<Boolean>> get() = mutableAcceptJourney
    private var mutableAcceptJourney: MutableLiveData<Resource<Boolean>> = MutableLiveData()

    val declineJourney: LiveData<Resource<Boolean>> get() = mutableDeclineJourney
    private var mutableDeclineJourney: MutableLiveData<Resource<Boolean>> = MutableLiveData()

    val navigateToJourneyPlans: LiveData<String> get() = mutableNavigation
    private var mutableNavigation: MutableLiveData<String> = MutableLiveData()

    fun loadPendingJourneys() {
        mutablePendingJourneys.value = Resource.Loading()
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
        mutableCurrentJourneys.value = Resource.Loading()
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
        mutableUpcomingJourneys.value = Resource.Loading()
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
        mutablePastJourneys.value = Resource.Loading()
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

    fun shareJourney(journeyId: String) {
        mutableShareJourney.postValue(journeyId)
    }

    fun acceptJourney(journeyId: String) {
        val observer = repository.acceptJourney(journeyId)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutableAcceptJourney.postValue(it)
                },
                onError = {
                    mutableAcceptJourney.postValue(Resource.Error(AppError(message = it.message)))
                }
            )
        subscriptions.add(observer)
    }

    fun deletePendingInvitation(journeyId: String) {
        val observer = repository.declineJourney(journeyId)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutableDeclineJourney.postValue(it)
                },
                onError = {
                    mutableDeclineJourney.postValue(Resource.Error(AppError(message = it.message)))
                }
            )
        subscriptions.add(observer)
    }

    fun handleNavigationToPlans(journeyId: String) {
        mutableNavigation.postValue(journeyId)
    }
}