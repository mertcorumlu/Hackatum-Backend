package de.tum.hack.Bloomberg.Challenge.api

data class FilterSnapshotsResponse(
        val card_id: String,
        val quantity: Int,
        val user_id: Int,
        val side: Boolean
)