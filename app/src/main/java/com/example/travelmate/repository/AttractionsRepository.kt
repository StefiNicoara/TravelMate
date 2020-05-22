package com.example.travelmate.repository

import android.net.Uri
import com.example.travelmate.model.Attraction
import com.example.travelmate.model.AttractionTag
import com.example.travelmate.model.City
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
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
}