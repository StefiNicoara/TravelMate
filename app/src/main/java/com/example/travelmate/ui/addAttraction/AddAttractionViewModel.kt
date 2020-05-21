package com.example.travelmate.ui.addAttraction

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelmate.R
import com.example.travelmate.model.AttractionTag
import com.example.travelmate.model.City
import com.example.travelmate.repository.AttractionsRepository
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers


class AddAttractionViewModel(private val repository: AttractionsRepository) : ViewModel() {

    lateinit var imageUri: Uri
    lateinit var imageExtension: String

    var title: ObservableField<String> = ObservableField()
    var city: ObservableField<String> = ObservableField()
    var country: ObservableField<String> = ObservableField()
    var description: ObservableField<String> = ObservableField()
    var tags: MutableList<AttractionTag> = mutableListOf()

    private val subscriptions = CompositeDisposable()

    val chooseImageClick: LiveData<Boolean> get() = mutableChooseImgClick
    private var mutableChooseImgClick: MutableLiveData<Boolean> = MutableLiveData()

    val publishAttraction: LiveData<Resource<Boolean>> get() = mutablePublishClick
    private var mutablePublishClick: MutableLiveData<Resource<Boolean>> = MutableLiveData()


    fun chooseImage() {
        mutableChooseImgClick.postValue(true)
    }

    private fun checkFieldsNotEmpty(): Boolean {
        if (title.get()?.isNotEmpty() == true &&
            city.get()?.isNotEmpty() == true &&
            country.get()?.isNotEmpty() == true &&
            description.get()?.isNotEmpty() == true
        ) {
            return true
        }
        return false
    }

    fun publishAttraction() {
        mutablePublishClick.value = Resource.Loading()

        if (checkFieldsNotEmpty()) {
            if (tags.size > 0) {
                callRepositoryFunction()
            } else {
                mutablePublishClick.value = Resource.Error(AppError(R.string.select_one_tag))
            }
        } else {
            mutablePublishClick.value = Resource.Error(AppError(R.string.no_empty_fields))
        }
    }

    private fun callRepositoryFunction() {
        val observer = repository.uploadAttraction(
            title.get().toString(),
            City(city.get().toString(), country.get().toString()),
            description.get().toString(),
            tags,
            imageUri,
            imageExtension
        )
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutablePublishClick.postValue(it)
                },
                onError = {
                    mutablePublishClick.postValue(Resource.Error(AppError(message = it.message)))
                }
            )
        subscriptions.add(observer)
    }
}