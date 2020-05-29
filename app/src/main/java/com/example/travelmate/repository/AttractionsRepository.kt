package com.example.travelmate.repository

import android.net.Uri
import com.example.travelmate.model.Attraction
import com.example.travelmate.model.AttractionTag
import com.example.travelmate.model.City
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Single
import android.widget.Toast
import com.example.travelmate.MainActivity
import com.google.android.gms.tasks.OnSuccessListener
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import com.example.travelmate.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers


class AttractionsRepository {

    private val fbAuth = FirebaseAuth.getInstance()
    private val storageReference = FirebaseStorage.getInstance().getReference("Attractions")
    private val db = FirebaseFirestore.getInstance()
    private val attractionsRef = db.collection("Attractions")
    private val usersRef = db.collection("Users")
    private val currentUserRef = fbAuth.currentUser?.uid?.let { usersRef.document(it) }

    private var currentUser: User = User()

    init {
        getCurrentUser()
    }

    fun uploadAttraction(
        title: String,
        city: City,
        description: String,
        tags: List<AttractionTag>,
        imageUri: Uri,
        fileExtension: String
    ): Single<Resource<Boolean>> {
        val fileReference =
            storageReference.child(System.currentTimeMillis().toString() + "." + fileExtension)

        return Single.create create@{ emitter ->
            fileReference.putFile(imageUri)
                .addOnSuccessListener { _ ->
                    fileReference.downloadUrl.addOnSuccessListener {
                        val attraction = Attraction(
                            id = "",
                            title = title,
                            city = city,
                            description = description,
                            tags = tags,
                            likes = 0,
                            image = it.toString(),
                            comments = null,
                            isLikedByCurrentUser = false,
                            isFavoriteByCurrentUser = false
                        )
                        attractionsRef.add(attraction)
                        emitter.onSuccess(
                            Resource.Success(true)
                        )
                    }
                }
                .addOnFailureListener {
                    emitter.onSuccess(Resource.Error(AppError(message = it.localizedMessage)))
                }
            return@create
        }
    }

    fun getAttractionById(attractionId: String): Single<Resource<Attraction>> {
        return Single.create create@{ emitter ->
            attractionsRef.document(attractionId).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val attraction = documentSnapshot.toObject(Attraction::class.java)
                        if (currentUser.favorites != null) {
                            if (currentUser.favorites?.contains(attractionId) == true) {
                                attraction?.isFavoriteByCurrentUser = true
                            }
                        }
                        attraction?.let { emitter.onSuccess(Resource.Success(it)) }
                    }
                }
                .addOnFailureListener {
                    emitter.onSuccess(Resource.Error(AppError(message = it.localizedMessage)))
                }
            return@create
        }
    }

    fun loadAllAttractions(): Single<Resource<List<Attraction>>> {
        val attractionsList = mutableListOf<Attraction>()
        return Single.create create@{ emitter ->
            attractionsRef.get()
                .addOnSuccessListener { queryDocumentSnapshot ->
                    for (documentSnapshot in queryDocumentSnapshot) {
                        var attraction = documentSnapshot.toObject(Attraction::class.java)

                        attraction = setUserPreferences(attraction, documentSnapshot.id)

                        attraction.id = documentSnapshot.id
                        attractionsList.add(attraction)
                    }
                    emitter.onSuccess(Resource.Success(attractionsList))
                }
                .addOnFailureListener {
                    emitter.onSuccess(Resource.Error(AppError(message = it.localizedMessage)))
                }
            return@create
        }
    }

    fun loadFavoriteAttractions(): Single<Resource<List<Attraction>>> {



//        getCurrentUser()
//        val attractionsList = mutableListOf<Attraction>()
//        return Single.create create@{ emitter ->
//
//            currentUser.favorites?.forEach { attractionId ->
//                attractionsRef.document(attractionId).get()
//                    .addOnSuccessListener { documentSnapshot ->
//                        if (documentSnapshot.exists()) {
//                            val attraction = documentSnapshot.toObject(Attraction::class.java)
//                            attraction?.id = documentSnapshot.id
//                            if (currentUser.likes != null) {
//                                if (currentUser.likes?.contains(attractionId) == true) {
//                                    attraction?.isLikedByCurrentUser = true
//                                }
//                            }
//                            attraction?.isFavoriteByCurrentUser = true
//                            attraction?.let {
//                                attractionsList.add(it)
//                            }
//                        }
//                        emitter.onSuccess(Resource.Success(attractionsList))
//                    }
//                    .addOnFailureListener {
//                        emitter.onSuccess(Resource.Error(AppError(message = it.localizedMessage)))
//                    }
//            }
//            return@create
//        }
    }

    fun filter(
        tags: List<AttractionTag>,
        searchTerm: String
    ): Single<Resource<List<Attraction>>> {
        return if (tags.isNotEmpty() && searchTerm != "") {
            filterByTagsAndSearch(tags, searchTerm)
        } else {
            if (tags.isEmpty() && searchTerm == "") {
                loadAllAttractions()
            } else {
                if (tags.isEmpty()) {
                    filterBySearch(searchTerm)
                } else {
                    filterByTags(tags)
                }
            }
        }
    }


    private fun filterByTags(tags: List<AttractionTag>): Single<Resource<List<Attraction>>> {
        val attractionsList = mutableListOf<Attraction>()
        return Single.create create@{ emitter ->
            attractionsRef.whereArrayContainsAny("tags", tags)
                .get()
                .addOnSuccessListener { queryDocumentSnapshot ->
                    for (documentSnapshot in queryDocumentSnapshot) {
                        var attraction = documentSnapshot.toObject(Attraction::class.java)
                        attraction = setUserPreferences(attraction, documentSnapshot.id)
                        attraction.id = documentSnapshot.id
                        attractionsList.add(attraction)
                    }
                    emitter.onSuccess(Resource.Success(attractionsList))
                }
                .addOnFailureListener {
                    emitter.onSuccess(Resource.Error(AppError(message = it.localizedMessage)))
                }
            return@create
        }
    }

    private fun filterBySearch(searchTerm: String): Single<Resource<List<Attraction>>> {
        val attractionsList = mutableListOf<Attraction>()
        return Single.create create@{ emitter ->

            val task1 = attractionsRef
                .orderBy("title")
                .startAt(searchTerm)
                .endAt("$searchTerm\uf8ff")
                .get()
            val task2 = attractionsRef
                .orderBy("city.name")
                .startAt(searchTerm)
                .endAt("$searchTerm\uf8ff")
                .get()
            val task3 = attractionsRef
                .orderBy("city.country")
                .startAt(searchTerm)
                .endAt("$searchTerm\uf8ff")
                .get()

            val tasks: Task<List<QuerySnapshot>> =
                Tasks.whenAllSuccess(task1, task2, task3)
            tasks.addOnSuccessListener {
                for (queryDocumentSnapshot in it) {
                    for (documentSnapshot in queryDocumentSnapshot) {
                        var attraction = documentSnapshot.toObject(Attraction::class.java)
                        attraction = setUserPreferences(attraction, documentSnapshot.id)
                        attraction.id = documentSnapshot.id
                        attractionsList.add(attraction)
                    }
                    emitter.onSuccess(Resource.Success(attractionsList))
                }
            }
                .addOnFailureListener {
                    emitter.onSuccess(Resource.Error(AppError(message = it.localizedMessage)))
                }
            return@create
        }
    }

    //check Algolia
    private fun filterByTagsAndSearch(
        tags: List<AttractionTag>,
        searchTerm: String
    ): Single<Resource<List<Attraction>>> {
        val attractionsList = mutableListOf<Attraction>()
        return Single.create create@{ emitter ->

            val task1 = attractionsRef
                .whereArrayContainsAny("tags", tags)
                .orderBy("title")
                .startAt(searchTerm)
                .endAt("$searchTerm\uf8ff")
                .get()
            val task2 = attractionsRef
                .whereArrayContainsAny("tags", tags)
                .orderBy("city.name")
                .startAt(searchTerm)
                .endAt("$searchTerm\uf8ff")
                .get()
            val task3 = attractionsRef
                .whereArrayContainsAny("tags", tags)
                .orderBy("city.country")
                .startAt(searchTerm)
                .endAt("$searchTerm\uf8ff")
                .get()


            val tasks: Task<List<QuerySnapshot>> =
                Tasks.whenAllSuccess(task1, task2, task3)
            tasks.addOnSuccessListener {
                for (queryDocumentSnapshot in it) {
                    for (documentSnapshot in queryDocumentSnapshot) {
                        var attraction = documentSnapshot.toObject(Attraction::class.java)
                        attraction = setUserPreferences(attraction, documentSnapshot.id)
                        attraction.id = documentSnapshot.id
                        attractionsList.add(attraction)
                    }
                    emitter.onSuccess(Resource.Success(attractionsList))
                }
            }
                .addOnFailureListener {
                    emitter.onSuccess(Resource.Error(AppError(message = it.localizedMessage)))
                }
            return@create
        }
    }

    fun likeAttractionTransaction(attractionId: String) {
        currentUserRef?.update("likes", FieldValue.arrayUnion(attractionId))
        db.runTransaction { transaction ->
            val attractionRef = attractionsRef.document(attractionId)
            val attractionSnapshot = transaction.get(attractionRef)
            val newLikesValue = attractionSnapshot.getLong("likes")?.plus(1)
            transaction.update(attractionRef, "likes", newLikesValue)
            newLikesValue
        }
    }

    fun undoLikeAttractionTransaction(attractionId: String) {
        currentUserRef?.update("likes", FieldValue.arrayRemove(attractionId))
        db.runTransaction { transaction ->
            val attractionRef = attractionsRef.document(attractionId)
            val attractionSnapshot = transaction.get(attractionRef)
            val newLikesValue = attractionSnapshot.getLong("likes")?.minus(1)
            transaction.update(attractionRef, "likes", newLikesValue)
            newLikesValue
        }
    }

    fun addAttractionToFavorites(attractionId: String) {
        currentUserRef?.update("favorites", FieldValue.arrayUnion(attractionId))
    }

    fun removeAttractionFromFavorites(attractionId: String) {
        currentUserRef?.update("favorites", FieldValue.arrayRemove(attractionId))
    }

    private fun setUserPreferences(attraction: Attraction, id: String): Attraction {
        if (currentUser.likes != null) {
            if (currentUser.likes?.contains(id) == true) {
                attraction.isLikedByCurrentUser = true
            }
        }

        if (currentUser.favorites != null) {
            if (currentUser.favorites?.contains(id) == true) {
                attraction.isFavoriteByCurrentUser = true
            }
        }
        return attraction
    }

    private fun getCurrentUser() {
        val userId = fbAuth.currentUser!!.uid

        usersRef.document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(User::class.java)
                    if (user != null) {
                        currentUser = user
                    }
                }
            }
    }


}