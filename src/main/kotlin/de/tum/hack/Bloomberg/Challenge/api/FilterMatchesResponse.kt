package de.tum.hack.Bloomberg.Challenge.api

data class FilterMatchesResponse(
        val price: Int,
        val quantity: Int,
        val side: Boolean,

)