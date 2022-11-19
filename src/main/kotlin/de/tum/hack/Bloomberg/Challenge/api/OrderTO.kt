package de.tum.hack.Bloomberg.Challenge.api

import de.tum.hack.Bloomberg.Challenge.models.Side

data class OrderTO(
    var cardId: String,
    var username: String,
    var quantity: Int,
    var price: Double,
    var side: Side
)
