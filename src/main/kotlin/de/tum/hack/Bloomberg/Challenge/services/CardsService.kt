package de.tum.hack.Bloomberg.Challenge.services

import de.tum.hack.Bloomberg.Challenge.models.Card
import de.tum.hack.Bloomberg.Challenge.repositories.CardRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Service
class CardsService(val cardsRepository: CardRepository) {

    fun search(q: String, sortBy: String): List<Card> {
        return emptyList()
    }

}
