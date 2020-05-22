package com.example.travelmate.model

data class Attraction(
    var title: String,
    var city: City,
    var description: String,
    var tags: List<AttractionTag>,
    var likes: Int,
    var image: String,
    var comments: List<Comment>?
) {
    constructor() : this("", City(), "", mutableListOf(), 0, "", mutableListOf())
}