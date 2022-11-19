package de.tum.hack.Bloomberg.Challenge.services

import de.tum.hack.Bloomberg.Challenge.api.FilterSortUserCardResponse
import de.tum.hack.Bloomberg.Challenge.api.SortTypes
import de.tum.hack.Bloomberg.Challenge.models.Card
import de.tum.hack.Bloomberg.Challenge.models.UserCard
import de.tum.hack.Bloomberg.Challenge.repositories.CardRepository
import de.tum.hack.Bloomberg.Challenge.repositories.UserCardRepository

class UserCardService(val userCardRepository: UserCardRepository, val cardRepository: CardRepository) {

    fun sortCards(userId: Int, sortBy: SortTypes): FilterSortUserCardResponse {
        val cardIds = getCardIds(userId)
        val sorted: List<Card>

        when (sortBy) {
            SortTypes.RARITY ->  sorted = cardRepository.findAllByCardIdInListAndOrderByRarity(cardIds)
            //SortTypes.PRICE ->
            else -> {
                sorted = cardRepository.findTop5ByCardIdInListAndOrderByNameAsc(cardIds)
            }
        }
        return FilterSortUserCardResponse(sorted, sorted.size)
    }

    fun getTop(userId: Int): FilterSortUserCardResponse {
        val cardIds = getCardIds(userId)
        val cards = cardRepository.findTop5ByCardIdInListAndOrderByNameAsc(cardIds)
        return FilterSortUserCardResponse(cards, cards.size)
    }

    private fun getCardIds(userId: Int): List<String> {
        val cardIds = arrayListOf<String>()
        userCardRepository.findAllCardByUserId(userId).forEach { userCard: UserCard ->
            val id = userCard.user.name
            if (id != null) {
                cardIds.add(id)
            }
        }
        return cardIds
    }
}