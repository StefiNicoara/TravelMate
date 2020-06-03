package com.example.travelmate.ui.profile.journeys

import android.net.Uri
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelmate.R
import com.example.travelmate.repository.JourneyRepository
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*

class AddJourneyViewModel(private val repository: JourneyRepository) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    var journeyName: ObservableField<String> = ObservableField()
    var startDate: Date? = null
    var endDate: Date? = null
    var imageUri: Uri? = null
    var imageExtension: String? = null

    val chooseImageClick: LiveData<Boolean> get() = mutableChooseImgClick
    private var mutableChooseImgClick: MutableLiveData<Boolean> = MutableLiveData()

    val createJourney: LiveData<Resource<Boolean>> get() = mutableCreateClick
    private var mutableCreateClick: MutableLiveData<Resource<Boolean>> = MutableLiveData()

    private fun checkFieldsNotEmpty(): Boolean {
        if (journeyName.get()?.isNotEmpty() == true &&
            startDate != null &&
            endDate != null &&
            imageUri != null
        ) {
            return true
        }
        return false
    }

    fun createJourney() {
        mutableCreateClick.value = Resource.Loading()
        if (checkFieldsNotEmpty()) {
            callRepositoryFunction()
        } else {
            mutableCreateClick.value =
                Resource.Error(AppError(message = "Sorry, fields can not be empty!"))
        }
    }

    private fun callRepositoryFunction() {
        val observer = repository.createJourney(
            journeyName.get().toString().removePrefix(" "),
            startDate!!,
            endDate!!,
            imageUri!!,
            imageExtension!!
        )
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutableCreateClick.postValue(it)
                },
                onError = {
                    mutableCreateClick.postValue(Resource.Error(AppError(message = it.message)))
                }
            )
        subscriptions.add(observer)
    }
}