package com.example.travelmate.model

import java.util.*

data class Comment(
    val username: String,
    val date: Date,
    val content: String
) {
    constructor() : this("", Date(), "")
}