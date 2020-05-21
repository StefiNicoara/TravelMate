package com.example.travelmate.model

import com.google.firebase.firestore.Exclude
import org.w3c.dom.Comment

data class Attraction(
    var title: String,
    var city: City,
    var description: String,
    var tags: List<AttractionTag>,
    var likes: Int,
    var image: String,
    var comments: List<Comment>?
)