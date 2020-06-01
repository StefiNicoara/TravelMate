package com.example.travelmate.repository

import android.net.Uri
import com.example.travelmate.model.Attraction
import com.example.travelmate.model.Journey
import com.example.travelmate.model.JourneyPlan
import com.example.travelmate.utils.AppError
import com.example.travelmate.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Single
import org.w3c.dom.Attr
import java.util.*

class JourneyRepository {

    private val fbAuth = FirebaseAuth.getInstance()
    private val storageReference = FirebaseStorage.getInstance().getReference("Journeys")
    private val db = FirebaseFirestore.getInstance()
    private val journeysRef = db.collection("Journeys")

    fun createJourney(
        name: String,
        startDate: Date,
        endDate: Date,
        imageUri: Uri,
        extension: String
    ): Single<Resource<Boolean>> {

        val fileReference =
            storageReference.child(System.currentTimeMillis().toString() + "." + extension)

        return Single.create create@{ emitter ->

            fileReference.putFile(imageUri)
                .addOnSuccessListener { _ ->
                    fileReference.downloadUrl.addOnSuccessListener {
                        val journey = Journey(
                            journeyId = "",
                            name = name,
                            startDate = startDate,
                            endDate = endDate,
                            image = it.toString(),
                            journeyPlans = null,
                            users = listOf(fbAuth.currentUser?.uid!!),
                            pending = null,
                            completed = false
                        )
                        journeysRef.add(journey).addOnSuccessListener { documentReference ->
                            documentReference.update("journeyId", documentReference.id)
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
}