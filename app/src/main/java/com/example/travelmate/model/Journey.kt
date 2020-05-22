package com.example.travelmate.model

data class Journey(
    var journeyId: String,
    var name: String,
    var attractions: List<AttractionProgress>
) {
    constructor() : this("", "", mutableListOf())
}