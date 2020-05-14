package com.example.travelmate.repository

import com.example.travelmate.model.User
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class UserRepository {

    private val fbAuth = FirebaseAuth.getInstance()

    fun registerUser(email: String, password: String): Single<Resource<Boolean>> {
        val register = Single.create<Resource<Boolean>> create@{ emitter ->
            fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onSuccess(
                        Resource.Success(true)
                    )
                } else {
                    emitter.onSuccess(Resource.Error(AppError(message = it.exception?.localizedMessage)))
                }
            }
            return@create
        }
        return register
            .observeOn(Schedulers.io())
            .flatMap { user ->
                Single.just(user)
            }
    }

    fun loginUser(email: String, password: String): Single<Resource<Boolean>> {
        val login: Single<Resource<Boolean>> = Single.create create@{ emitter ->
            fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onSuccess(
                        Resource.Success(true)
                    )
                } else {
                    emitter.onSuccess(Resource.Error(AppError(message = it.exception?.localizedMessage.toString())))
                }
            }
            return@create
        }
        return login.observeOn(Schedulers.io()).flatMap { bool ->
            Single.just(bool)
        }
    }
}