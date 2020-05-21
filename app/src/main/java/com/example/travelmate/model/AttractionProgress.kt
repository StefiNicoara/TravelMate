package com.example.travelmate.model

data class AttractionProgress (
    var aProgressId: String,
    var attraction: Attraction,
    var complete: Boolean = false
)