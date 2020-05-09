package com.example.travelmate.ui.account.register

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelmate.R
import com.example.travelmate.model.User
import com.example.travelmate.repository.UserRepository
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    var nickname: ObservableField<String> = ObservableField()
    var email: ObservableField<String> = ObservableField()
    var password: ObservableField<String> = ObservableField()
    var confirmPassword: ObservableField<String> = ObservableField()

    private val subscriptions = CompositeDisposable()

    val userModel: LiveData<Resource<User>> get() = mutableUserModel
    private var mutableUserModel: MutableLiveData<Resource<User>> = MutableLiveData()


    private fun checkFieldsNotEmpty(): Boolean {
        if (nickname.get()?.isNotEmpty() == true &&
            email.get()?.isNotEmpty() == true &&
            password.get()?.isNotEmpty() == true &&
            confirmPassword.get()?.isNotEmpty() == true
        ) {
            return true
        }
        return false
    }

    private fun checkPasswordsMatch(): Boolean {
        return password.get().toString() == confirmPassword.get().toString()
    }

    private fun callRepositoryFunction() {
        val observer =
            repository.registerUser(
                nickname.get().toString(),
                email.get().toString(),
                password.get().toString()
            )
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onSuccess = {
                        mutableUserModel.postValue(it)
                    },
                    onError = {
                        mutableUserModel.postValue(Resource.Error(AppError(message = it.message)))
                    }
                )
        subscriptions.add(observer)
    }

    fun registerUser() {
        mutableUserModel.value = Resource.Loading()

        if (checkFieldsNotEmpty()) {
            if (checkPasswordsMatch()) {
                callRepositoryFunction()
            } else {
                mutableUserModel.value = Resource.Error(AppError(R.string.no_matching_passwords))
            }
        } else {
            mutableUserModel.value = Resource.Error(AppError(R.string.no_empty_fields))
        }
    }

}