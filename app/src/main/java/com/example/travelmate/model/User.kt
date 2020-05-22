package com.example.travelmate.model

import com.google.firebase.firestore.Exclude

data class User (
    @Exclude
    val userId: String,
    var nickname: String,
    var email : String,
    var journeys: List<Journey>?,
    var favorites: List<Attraction>?,
    var uploads: List<Attraction>?,
    var visited: List<City>?
)  {
    constructor() : this("", "", "", mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())
}