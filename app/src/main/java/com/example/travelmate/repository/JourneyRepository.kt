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

    fun getCurrentJourneys(): Single<Resource<List<Journey>>> {

        val journeyList = mutableListOf<Journey>()

        return Single.create create@{ emitter ->
            journeysRef.whereArrayContains("users", fbAuth.currentUser!!.uid)
                .whereEqualTo("started", true)
                .get()
                .addOnSuccessListener { queryDocumentSnapshot ->
                    for (documentSnapshot in queryDocumentSnapshot) {
                        val journey = documentSnapshot.toObject(Journey::class.java)
                        journeyList.add(journey)
                    }
                    emitter.onSuccess(Resource.Success(journeyList))
                }
                .addOnFailureListener {
                    emitter.onSuccess(Resource.Error(AppError(message = it.localizedMessage)))
                }
            return@create
        }
    }

    fun getUpcomingJourneys(): Single<Resource<List<Journey>>> {
        val journeyList = mutableListOf<Journey>()

        return Single.create create@{ emitter ->
            journeysRef.whereArrayContains("users", fbAuth.currentUser!!.uid)
                .whereGreaterThanOrEqualTo("startDate", Date())
                .whereEqualTo("started", false)
                .get()
                .addOnSuccessListener { queryDocumentSnapshot ->
                    for (documentSnapshot in queryDocumentSnapshot) {
                        val journey = documentSnapshot.toObject(Journey::class.java)
                        journeyList.add(journey)
                    }
                    emitter.onSuccess(Resource.Success(journeyList))
                }
                .addOnFailureListener {
                    emitter.onSuccess(Resource.Error(AppError(message = it.localizedMessage)))
                }
            return@create
        }
    }

    fun getPastJourneys(): Single<Resource<List<Journey>>> {
        val journeyList = mutableListOf<Journey>()

        return Single.create create@{ emitter ->
            journeysRef.whereArrayContains("users", fbAuth.currentUser!!.uid)
                .whereLessThan("startDate", Date())
                .get()
                .addOnSuccessListener { queryDocumentSnapshot ->
                    for (documentSnapshot in queryDocumentSnapshot) {
                        val journey = documentSnapshot.toObject(Journey::class.java)
                        journeyList.add(journey)
                    }
                    emitter.onSuccess(Resource.Success(journeyList))
                }
                .addOnFailureListener {
                    emitter.onSuccess(Resource.Error(AppError(message = it.localizedMessage)))
                }
            return@create
        }
    }

    fun getPendingJourneys(): Single<Resource<List<Journey>>> {
        val journeyList = mutableListOf<Journey>()

        return Single.create create@{ emitter ->
            journeysRef.whereArrayContains("pending", fbAuth.currentUser!!.uid)
                .get()
                .addOnSuccessListener { queryDocumentSnapshot ->
                    for (documentSnapshot in queryDocumentSnapshot) {
                        val journey = documentSnapshot.toObject(Journey::class.java)
                        journeyList.add(journey)
                    }
                    emitter.onSuccess(Resource.Success(journeyList))
                }
                .addOnFailureListener {
                    emitter.onSuccess(Resource.Error(AppError(message = it.localizedMessage)))
                }
            return@create
        }
    }

    fun startJourney(journeyId: String): Single<Resource<Boolean>> {
        return Single.create create@{ emitter ->
            journeysRef.document(journeyId)
                .update("started", true)
                .addOnSuccessListener {
                    emitter.onSuccess(Resource.Success(true))
                }
                .addOnFailureListener {
                    emitter.onSuccess(Resource.Error(AppError(message = it.localizedMessage)))
                }
            return@create
        }
    }
}