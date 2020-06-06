package com.example.travelmate.ui.account.login.resetPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelmate.repository.UserAccountRepository
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ForgotPasswordViewModel(private val repository: UserAccountRepository) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    val sendEmail: LiveData<Resource<Boolean>> get() = mutableSendEmail
    private var mutableSendEmail: MutableLiveData<Resource<Boolean>> =
        MutableLiveData()

    fun forgotPassword(email: String?) {
        mutableSendEmail.postValue(Resource.Loading())

        if (email != null) {
            val observer = repository.forgotPassword(email)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onSuccess = {
                        mutableSendEmail.postValue(it)
                    },
                    onError = {
                        mutableSendEmail.postValue(Resource.Error(AppError(message = it.message)))
                    }
                )
            subscriptions.add(observer)
        } else {
            mutableSendEmail.postValue(Resource.Error(AppError(message = "You must provide an email address!")))
        }
    }
}