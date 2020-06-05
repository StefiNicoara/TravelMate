package com.example.travelmate.model

import com.google.firebase.firestore.Exclude

data class User(
    @Exclude
    val userId: String,
    val username: String,
    var nickname: String,
    var email: String,
    var journeys: List<Journey>?,
    var favorites: List<String>?,
    var likes: List<String>?
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        mutableListOf(),
        mutableListOf(),
        mutableListOf()
    )
}