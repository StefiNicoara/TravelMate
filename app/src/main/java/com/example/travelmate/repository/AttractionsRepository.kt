package com.example.travelmate.repository

import android.net.Uri
import com.example.travelmate.model.Attraction
import com.example.travelmate.model.AttractionTag
import com.example.travelmate.model.City
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Single

class AttractionsRepository {

    private val storageReference = FirebaseStorage.getInstance().getReference("Attractions")
    private val db = FirebaseFirestore.getInstance()
    private val attractionsRef = db.collection("Attractions")

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
                            title = title,
                            city = city,
                            description = description,
                            tags = tags,
                            likes = 0,
                            image = it.toString(),
                            comments = null
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

    fun loadAllAttractions(): Single<Resource<List<Attraction>>> {
        val attractionsList = mutableListOf<Attraction>()
        return Single.create create@{ emitter ->
            attractionsRef.get()
                .addOnSuccessListener { queryDocumentSnapshot ->
                    for (documentSnapshot in queryDocumentSnapshot) {
                        val attraction = documentSnapshot.toObject(Attraction::class.java)
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


    fun filter(tags: List<AttractionTag>, searchTerm: String): Single<Resource<List<Attraction>>> {
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
}