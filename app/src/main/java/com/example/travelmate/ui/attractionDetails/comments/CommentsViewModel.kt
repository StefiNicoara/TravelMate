package com.example.travelmate.ui.attractionDetails.comments

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelmate.model.Comment
import com.example.travelmate.repository.AttractionsRepository
import com.example.travelmate.repository.UserAccountRepository
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*

class CommentsViewModel(
    private var repository: AttractionsRepository,
    private val userRepository: UserAccountRepository
) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    val loadComments: LiveData<Resource<List<Comment>>> get() = mutableCommentsList
    private var mutableCommentsList: MutableLiveData<Resource<List<Comment>>> =
        MutableLiveData()

    val username: LiveData<String> get() = mutableUsername
    private var mutableUsername: MutableLiveData<String> =
        MutableLiveData()

    val addCommentResponse: LiveData<Resource<Boolean>> get() = mutableAddComment
    private var mutableAddComment: MutableLiveData<Resource<Boolean>> =
        MutableLiveData()


    fun getComments(attractionId: String) {
        mutableCommentsList.postValue(Resource.Loading())
        val observer = repository.getComments(attractionId)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutableCommentsList.postValue(it)
                },
                onError = {
                    mutableCommentsList.postValue(Resource.Error(AppError(message = it.message)))
                }
            )
        subscriptions.add(observer)
    }

    fun addComment(username: String, attractionId: String, commentText: String?) {
        mutableAddComment.postValue(Resource.Loading())
        if (commentText != null) {
            val comment = Comment(username, Date(), commentText)
            val observer = repository.addComment(attractionId, comment)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onSuccess = {
                        mutableAddComment.postValue(it)
                    },
                    onError = {
                        mutableAddComment.postValue(Resource.Error(AppError(message = it.message)))
                    }
                )
            subscriptions.add(observer)
        } else {
            mutableAddComment.postValue(Resource.Error(AppError(message = "Fields can not be empty!")))
        }
    }

    fun getCurrentUsername() {
        val observer = userRepository.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    mutableUsername.postValue(it.username)
                }
            )
        subscriptions.add(observer)

    }
}