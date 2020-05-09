package com.example.travelmate.repository

import com.example.travelmate.model.User
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class UserRepository() {

    private val fbAuth = FirebaseAuth.getInstance()

    fun registerUser(nickname: String, email: String, password: String): Single<Resource<User>> {
        val register = Single.create<Resource<User>> create@{ emitter ->
            fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = User(
                        userId = fbAuth.uid.toString(),
                        nickname = nickname,
                        email = email
                    )
                    emitter.onSuccess(
                        Resource.Success(user)
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
}