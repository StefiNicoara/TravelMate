package com.example.travelmate.model

import java.util.*

data class JourneyPlan(
    var attraction: Attraction,
    var date: Date,
    var complete: Boolean = false
) {
    constructor() : this(
        Attraction(),
        Date(),
        false
    )
}