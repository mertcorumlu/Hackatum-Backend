package de.tum.hack.Bloomberg.Challenge.services

import de.tum.hack.Bloomberg.Challenge.api.FilterSortUserCardResponse
import de.tum.hack.Bloomberg.Challenge.api.SortTypes
import de.tum.hack.Bloomberg.Challenge.models.Card
import de.tum.hack.Bloomberg.Challenge.repositories.CardRepository
import de.tum.hack.Bloomberg.Challenge.repositories.UserCardRepository
import org.springframework.stereotype.Service

@Service
class UserCardService(val cardRepository: CardRepository) {

    fun sortCards(userId: Int, sortBy: SortTypes): FilterSortUserCardResponse {

        val sorted: List<Card> = when (sortBy) {
            SortTypes.RARITY -> cardRepository.findAllByCardIdInListOrderByRarity(userId)
            else -> cardRepository.findAllByCardIdInListOrderByNameAsc(userId)
        }
        return FilterSortUserCardResponse(sorted, sorted.size)
    }

    fun getTop(userId: Int): FilterSortUserCardResponse {
        val cards = cardRepository.findTop5ByCardIdInListOrderByNameAsc(userId)
        return FilterSortUserCardResponse(cards, cards.size)
    }
}
