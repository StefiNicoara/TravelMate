package com.example.travelmate.model

import java.util.*

data class Attraction(
    var id: String,
    var author: String,
    var title: String,
    var city: City,
    var latitude: Double?,
    var longitude: Double?,
    var description: String,
    var tags: List<AttractionTag>,
    var image: String,
    var likes: Int,
    var publishDate: Date,
    var favoriteBy: List<String>?,
    var comments: List<Comment>?
) {
    constructor() : this(
        id = "",
        author = "",
        title = "",
        city = City(),
        latitude = null,
        longitude = null,
        description = "",
        tags = mutableListOf(),
        likes = 0,
        image = "",
        publishDate = Date(),
        favoriteBy = mutableListOf(),
        comments = mutableListOf()
    )
}