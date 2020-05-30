package com.example.travelmate.model

import com.google.firebase.firestore.Exclude

data class Attraction(
    var id: String,
    var author: String,
    var title: String,
    var city: City,
    var description: String,
    var tags: List<AttractionTag>,
    var image: String,
    var likes: Int,
    var favoriteBy: List<String>?,
    var comments: List<Comment>?,
    @get:Exclude
    var isLikedByCurrentUser: Boolean,
    var isFavoriteByCurrentUser: Boolean
) {
    constructor() : this(
        id = "",
        author = "",
        title = "",
        city = City(),
        description = "",
        tags = mutableListOf(),
        likes = 0,
        image = "",
        favoriteBy = mutableListOf(),
        comments = mutableListOf(),
        isFavoriteByCurrentUser = false,
        isLikedByCurrentUser = false
    )
}