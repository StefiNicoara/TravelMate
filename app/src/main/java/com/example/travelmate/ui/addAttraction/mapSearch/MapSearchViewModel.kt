package com.example.travelmate.ui.addAttraction.mapSearch

import android.location.Address
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelmate.model.City
import com.example.travelmate.repository.AttractionsRepository
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MapSearchViewModel(private val repository: AttractionsRepository) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    val updateAttraction: LiveData<Resource<Boolean>> get() = mutableUpdateAttraction
    private var mutableUpdateAttraction: MutableLiveData<Resource<Boolean>> = MutableLiveData()

    fun updateAttraction(attractionId: String, address: Address) {
        val city = City(address.locality ?: "", address.countryName ?: "")
        val longitude = address.longitude
        val latitude = address.latitude

        mutableUpdateAttraction.postValue(Resource.Loading())
        val observer = repository.updateAttraction(attractionId, city, latitude, longitude)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutableUpdateAttraction.postValue(it)
                },
                onError = {
                    mutableUpdateAttraction.postValue(Resource.Error(AppError(message = it.message)))
                }
            )
        subscriptions.add(observer)
    }
}