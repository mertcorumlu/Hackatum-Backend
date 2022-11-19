package de.tum.hack.Bloomberg.Challenge.api

data class FilterSnapshotsResponse(
        val card_id: String,
        val quantity: Int,
        val side: Boolean,
        val user_id1: Int,
        val user_id2: Int,
)