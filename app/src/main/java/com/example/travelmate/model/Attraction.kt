package com.example.travelmate.model

import com.google.firebase.firestore.Exclude

data class Attraction(
    @get:Exclude
    var id: String,
    var title: String,
    var city: City,
    var description: String,
    var tags: List<AttractionTag>,
    var likes: Int,
    var image: String,
    var comments: List<Comment>?,
    @get:Exclude
    var isLikedByCurrentUser: Boolean,
    var isFavoriteByCurrentUser: Boolean
) {
    constructor() : this("", "", City(), "", mutableListOf(), 0, "", mutableListOf(), false, false)
}