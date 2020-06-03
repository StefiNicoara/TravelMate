package com.example.travelmate.ui.createJourneyPlan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelmate.model.Attraction
import com.example.travelmate.model.Journey
import com.example.travelmate.model.JourneyPlan
import com.example.travelmate.repository.AttractionsRepository
import com.example.travelmate.repository.JourneyRepository
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*

class CreateJourneyPlanViewModel(
    private val journeyRepository: JourneyRepository,
    private val attractionRepository: AttractionsRepository
) : ViewModel() {

    var attractionId: String = ""
    var journeyId: String? = null
    val calendar: Calendar? = Calendar.getInstance()
    var attraction: Attraction? = null

    private val subscriptions = CompositeDisposable()

    val attractionAdded: LiveData<Resource<Boolean>> get() = mutableAttractionAdded
    private var mutableAttractionAdded: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val loadJourneys: LiveData<Resource<List<Journey>>> get() = mutableJourneys
    private var mutableJourneys: MutableLiveData<Resource<List<Journey>>> =
        MutableLiveData()


    fun getCurrentAttraction(attractionId: String) {
        val observer = attractionRepository.getAttractionById(attractionId)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    attraction = it.data
                }
            )
        subscriptions.add(observer)
    }

    fun addAttractionToPlan() {
        val journeyPlan =
            calendar?.time?.let { attraction?.let { it1 -> JourneyPlan(it1, it, false) } }

        if (journeyPlan != null && journeyId != null) {
            val observer = journeyRepository.addAttractionToJourneyPlan(journeyId!!, journeyPlan)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onSuccess = {
                        mutableAttractionAdded.postValue(it)
                    },
                    onError = {
                        mutableAttractionAdded.postValue(Resource.Error(AppError(message = it.message)))
                    }
                )
            subscriptions.add(observer)
        } else {
            mutableAttractionAdded.postValue(Resource.Error(AppError(message = "Please select all fields!")))
        }
    }

    fun loadJourneys() {
        val observer = journeyRepository.getAvailableJourneys()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutableJourneys.postValue(it)
                },
                onError = {
                    mutableJourneys.postValue(Resource.Error(AppError(message = it.message)))
                }
            )
        subscriptions.add(observer)
    }
}