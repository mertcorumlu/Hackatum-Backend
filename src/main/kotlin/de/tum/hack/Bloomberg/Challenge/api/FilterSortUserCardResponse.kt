package de.tum.hack.Bloomberg.Challenge.api

import de.tum.hack.Bloomberg.Challenge.models.Card

data class FilterSortUserCardResponse(
        val card: List<Card>,
        val amount: Int
)