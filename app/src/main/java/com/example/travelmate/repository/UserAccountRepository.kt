package com.example.travelmate.repository

import com.example.travelmate.model.User
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import com.google.firebase.firestore.FirebaseFirestore


class UserAccountRepository {

    private val fbAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val usersRef = db.collection("Users")


    fun registerUser(nickname: String, email: String, password: String): Single<Resource<Boolean>> {
        return Single.create create@{ emitter ->
            fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = User(
                        userId = it.result?.user!!.uid,
                        nickname = nickname,
                        email = email,
                        journeys = null,
                        likes = null,
                        favorites = null
                    )
                    usersRef.document(it.result?.user!!.uid).set(user)
                    emitter.onSuccess(
                        Resource.Success(true)
                    )
                } else {
                    emitter.onSuccess(Resource.Error(AppError(message = it.exception?.localizedMessage)))
                }
            }
            return@create
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

    fun logOutUser(): Single<Resource<Boolean>> {
        return Single.create create@{ emitter ->
            fbAuth.signOut()
            emitter.onSuccess(Resource.Success(true))
            return@create
        }
    }

    fun getCurrentUser(): Single<User> {
        val userId = fbAuth.currentUser!!.uid

        return Single.create create@{ emitter ->
            usersRef.document(userId).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val user = documentSnapshot.toObject(User::class.java)
                        user?.let { emitter.onSuccess(it) }
                    }

                }
            return@create
        }
    }
}