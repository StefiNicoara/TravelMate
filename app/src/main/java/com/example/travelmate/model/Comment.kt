package com.example.travelmate.model

data class Comment(
    val user: User,
    val content: String
) {
    constructor() : this(User(), "")
}