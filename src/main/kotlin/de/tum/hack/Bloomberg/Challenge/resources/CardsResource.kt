package de.tum.hack.Bloomberg.Challenge.resources

import de.tum.hack.Bloomberg.Challenge.models.Card
import de.tum.hack.Bloomberg.Challenge.services.CardsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController("/api/cards")
class CardsResource(val cardsService: CardsService) {

    @GetMapping("")
    fun getCards(
        @RequestParam q: String,
        @RequestParam sortBy: String
    ): List<Card> {
        return cardsService.search(q, sortBy)
    }
}
