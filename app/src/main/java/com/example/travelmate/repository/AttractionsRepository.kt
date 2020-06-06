package com.example.travelmate.repository

import android.net.Uri
import com.example.travelmate.model.*
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Single
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*


@Suppress("UNCHECKED_CAST")
class AttractionsRepository {

    private val fbAuth = FirebaseAuth.getInstance()
    private val storageReference = FirebaseStorage.getInstance().getReference("Attractions")
    private val db = FirebaseFirestore.getInstance()
    private val attractionsRef = db.collection("Attractions")
    private val usersRef = db.collection("Users")

    private var currentUser: User = User()


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
                            author = fbAuth.currentUser!!.uid,
                            title = title,
                            city = city,
                            description = description,
                            tags = tags,
                            likes = 0,
                            favoriteBy = null,
                            image = it.toString(),
                            comments = null,
                            isLikedByCurrentUser = false,
                            isFavoriteByCurrentUser = false
                        )
                        attractionsRef.add(attraction).addOnSuccessListener { documentReference ->
                            documentReference.update("id", documentReference.id)
                        }
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
        val userId = fbAuth.currentUser!!.uid

        return Single.create create@{ emitter ->
            usersRef.document(userId).get()
                .addOnSuccessListener { documentSnapshotUser ->
                    if (documentSnapshotUser.exists()) {
                        val user = documentSnapshotUser.toObject(User::class.java)
                        if (user != null) {
                            currentUser = user
                            attractionsRef.get()
                                .addOnSuccessListener { queryDocumentSnapshot ->
                                    for (documentSnapshot in queryDocumentSnapshot) {
                                        val attraction =
                                            documentSnapshot.toObject(Attraction::class.java)
                                        setUserPreferences(attraction, user)
                                        attractionsList.add(attraction)
                                    }
                                    emitter.onSuccess(Resource.Success(attractionsList))
                                }
                                .addOnFailureListener {
                                    emitter.onSuccess(Resource.Error(AppError(message = it.localizedMessage)))
                                }
                        }
                    }
                }
        }
    }

    fun loadFavoriteAttractions(): Single<Resource<List<Attraction>>> {
        val userId = fbAuth.currentUser!!.uid
        val attractionsList = mutableListOf<Attraction>()
        return Single.create create@{ emitter ->
            attractionsRef.whereArrayContains("favoriteBy", userId)
                .get()
                .addOnSuccessListener { queryDocumentSnapshot ->
                    for (documentSnapshot in queryDocumentSnapshot) {
                        val attraction = documentSnapshot.toObject(Attraction::class.java)
                        setUserLikes(attraction, currentUser)
                        attraction.isFavoriteByCurrentUser = true
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

    fun loadUploadedAttractions(): Single<Resource<List<Attraction>>> {
        val userId = fbAuth.currentUser!!.uid
        val attractionsList = mutableListOf<Attraction>()
        return Single.create create@{ emitter ->
            attractionsRef.whereEqualTo("author", userId)
                .get()
                .addOnSuccessListener { queryDocumentSnapshot ->
                    for (documentSnapshot in queryDocumentSnapshot) {
                        val attraction = documentSnapshot.toObject(Attraction::class.java)
                        setUserPreferences(attraction, currentUser)
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
                        val attraction = documentSnapshot.toObject(Attraction::class.java)
                        setUserPreferences(attraction, currentUser)
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
                        val attraction = documentSnapshot.toObject(Attraction::class.java)
                        setUserPreferences(attraction, currentUser)
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
                        val attraction = documentSnapshot.toObject(Attraction::class.java)
                        setUserPreferences(attraction, currentUser)
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

    fun getComments(attractionId: String): Single<Resource<List<Comment>>> {
        return Single.create create@{ emitter ->
            attractionsRef.document(attractionId).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val attraction = documentSnapshot.toObject(Attraction::class.java)
                        if (attraction?.comments == null) {
                            emitter.onSuccess(Resource.Success(mutableListOf()))
                        } else {
                            emitter.onSuccess(Resource.Success(attraction.comments!!))
                        }
                    }
                }
                .addOnFailureListener {
                    emitter.onSuccess(Resource.Error(AppError(message = it.localizedMessage)))
                }
            return@create
        }
    }

    fun addComment(attractionId: String, comment: Comment): Single<Resource<Boolean>> {
        return Single.create create@{ emitter ->
            attractionsRef.document(attractionId)
                .update("comments", FieldValue.arrayUnion(comment))
                .addOnSuccessListener {
                    emitter.onSuccess(Resource.Success(true))
                }
                .addOnFailureListener {
                    emitter.onSuccess(Resource.Error(AppError(message = it.localizedMessage)))
                }
            return@create
        }
    }

    fun likeAttractionTransaction(attractionId: String) {

        val currentUserRef = fbAuth.currentUser?.uid?.let { usersRef.document(it) }
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

        val currentUserRef = fbAuth.currentUser?.uid?.let { usersRef.document(it) }
        currentUserRef?.update("likes", FieldValue.arrayRemove(attractionId))
        db.runTransaction { transaction ->
            val attractionRef = attractionsRef.document(attractionId)
            val attractionSnapshot = transaction.get(attractionRef)
            val newLikesValue = attractionSnapshot.getLong("likes")?.minus(1)
            transaction.update(attractionRef, "likes", newLikesValue)
            newLikesValue
        }
    }

    fun addAttractionToFavorites(attraction: Attraction) {
        val currentUserRef = fbAuth.currentUser?.uid?.let { usersRef.document(it) }
        currentUserRef?.update("favorites", FieldValue.arrayUnion(attraction.id))
        attractionsRef.document(attraction.id)
            .update("favoriteBy", FieldValue.arrayUnion(fbAuth.currentUser!!.uid))
    }

    fun removeAttractionFromFavorites(attraction: Attraction) {
        val currentUserRef = fbAuth.currentUser?.uid?.let { usersRef.document(it) }
        currentUserRef?.update("favorites", FieldValue.arrayRemove(attraction.id))
        attractionsRef.document(attraction.id)
            .update("favoriteBy", FieldValue.arrayRemove(fbAuth.currentUser!!.uid))
    }


    private fun setUserLikes(attraction: Attraction, user: User) {
        if (user.likes != null) {
            if (user.likes?.contains(attraction.id) == true) {
                attraction.isLikedByCurrentUser = true
            }
        }
    }

    private fun setUserPreferences(attraction: Attraction, user: User) {
        setUserLikes(attraction, user)

        if (user.favorites != null) {
            if (user.favorites?.contains(attraction.id) == true) {
                attraction.isFavoriteByCurrentUser = true
            }
        }
    }
}