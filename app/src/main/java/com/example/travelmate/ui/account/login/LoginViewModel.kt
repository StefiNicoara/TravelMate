package com.example.travelmate.ui.account.login

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelmate.R
import com.example.travelmate.repository.UserRepository
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    var email: ObservableField<String> = ObservableField()
    var password: ObservableField<String> = ObservableField()

    private val subscriptions = CompositeDisposable()

    val logInResponse: LiveData<Resource<Boolean>> get() = mutableLoginResponse
    private var mutableLoginResponse: MutableLiveData<Resource<Boolean>> = MutableLiveData()

    private fun checkFieldsNotEmpty(): Boolean {
        if (email.get()?.isNotEmpty() == true && password.get()?.isNotEmpty() == true) {
            return true
        }
        return false
    }

    private fun callRepositoryFunction() {
        val observer =
            repository.loginUser(
                email.get().toString(),
                password.get().toString()
            )
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onSuccess = {
                        mutableLoginResponse.postValue(it)
                    },
                    onError = {
                        mutableLoginResponse.postValue(Resource.Error(AppError(message = it.message)))
                    }
                )
        subscriptions.add(observer)
    }

    fun loginUser() {
        Log.d("CEVA", "clicked")
        mutableLoginResponse.value = Resource.Loading()
        if (checkFieldsNotEmpty()) {
            callRepositoryFunction()
        } else {
            mutableLoginResponse.value = Resource.Error(AppError(R.string.no_empty_fields))
        }
    }
}