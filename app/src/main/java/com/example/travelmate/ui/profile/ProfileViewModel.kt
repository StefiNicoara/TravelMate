package com.example.travelmate.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelmate.model.Journey
import com.example.travelmate.model.User
import com.example.travelmate.repository.UserAccountRepository
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ProfileViewModel(private val repository: UserAccountRepository) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    val user: LiveData<User> get() = mutableUser
    private var mutableUser: MutableLiveData<User> =
        MutableLiveData()

    val logOut: LiveData<Resource<Boolean>> get() = mutableLogOut
    private var mutableLogOut: MutableLiveData<Resource<Boolean>> =
        MutableLiveData()


    fun getCurrentUser() {
        val observer = repository.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutableUser.postValue(it)
                }
            )
        subscriptions.add(observer)
    }

    fun logOut() {
        mutableLogOut.value = Resource.Loading()
        val observer = repository.logOutUser()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutableLogOut.postValue(it)
                },
                onError = {
                    mutableLogOut.postValue(Resource.Error(AppError(message = it.message)))
                }
            )
        subscriptions.add(observer)
    }

    fun changePassword() {

    }
}