package com.example.travelmate.ui.profile.journeys.journeyPlan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelmate.model.Journey
import com.example.travelmate.model.JourneyPlan
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

    val deleteResponse: LiveData<Resource<Boolean>> get() = mutableDeleteResponse
    private var mutableDeleteResponse: MutableLiveData<Resource<Boolean>> =
        MutableLiveData()

    val markCompleteResponse: LiveData<Resource<Boolean>> get() = mutableMarkComplete
    private var mutableMarkComplete: MutableLiveData<Resource<Boolean>> =
        MutableLiveData()


    fun loadJourney(journeyId: String) {
        mutableJourney.value = Resource.Loading()
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

    fun deletePlan(plan: JourneyPlan, journeyId: String) {
        val observer = repository.deleteJourneyPlan(plan, journeyId)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutableDeleteResponse.postValue(it)
                },
                onError = {
                    mutableDeleteResponse.postValue(Resource.Error(AppError(message = it.message)))
                }
            )
        subscriptions.add(observer)
    }

    fun markJourneyComplete(journeyId: String) {
        val observer = repository.markJourneyComplete(journeyId)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutableMarkComplete.postValue(it)
                },
                onError = {
                    mutableMarkComplete.postValue(Resource.Error(AppError(message = it.message)))
                }
            )
        subscriptions.add(observer)
    }
}