package com.example.travelmate.ui.profile.journeys.shareJourney

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelmate.R
import com.example.travelmate.model.User
import com.example.travelmate.repository.JourneyRepository
import com.example.travelmate.repository.UserAccountRepository
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ShareJourneyViewModel(
    private val userRepository: UserAccountRepository,
    private val journeyRepository: JourneyRepository
) : ViewModel() {
    var userName: ObservableField<String> = ObservableField()

    private val subscriptions = CompositeDisposable()

    val searchClick: LiveData<Resource<User>> get() = mutableSearchClick
    private var mutableSearchClick: MutableLiveData<Resource<User>> = MutableLiveData()

    val sendClick: LiveData<Resource<Boolean>> get() = mutableSendClick
    private var mutableSendClick: MutableLiveData<Resource<Boolean>> = MutableLiveData()

    fun searchByUsername() {
        if (userName.get() != null) {
            val observer = userRepository.searchByUsername(userName.get()!!)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onSuccess = {
                        mutableSearchClick.postValue(it)
                    },
                    onError = {
                        mutableSearchClick.postValue(Resource.Error(AppError(message = it.message)))
                    }
                )
            subscriptions.add(observer)
        } else {
            mutableSearchClick.postValue(Resource.Error(AppError(R.string.no_empty_fields)))
        }
    }

    fun sendToUser(journeyId: String, userId: String) {
        val observer = journeyRepository.shareJourney(journeyId, userId)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutableSendClick.postValue(it)
                },
                onError = {
                    mutableSendClick.postValue(Resource.Error(AppError(message = it.message)))
                }
            )
        subscriptions.add(observer)
    }

}