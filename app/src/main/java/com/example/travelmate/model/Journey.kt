package com.example.travelmate.model

import java.util.*

data class Journey(
    var journeyId: String,
    var name: String,
    var startDate: Date,
    var endDate: Date,
    var image: String?,
    var journeyPlans: List<JourneyPlan>?,
    var users: List<String>,
    var pending: List<String>?,
    var completed: Boolean = false,
    var started: Boolean = false
) {
    constructor() : this(
        "",
        "",
        Date(),
        Date(),
        null,
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        false,
        false
    )
}