package de.tum.hack.Bloomberg.Challenge.resources

import de.tum.hack.Bloomberg.Challenge.services.CardsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController("/api/cards")
class CardsResource(val cardsService: CardsService) {

    @GetMapping("")
    fun getCards(
        @RequestParam q: String?,
        @RequestParam sortBy: String?
    ) = cardsService.search(q, sortBy)

    @GetMapping("/{id}")
    fun getCard(@PathVariable("id") card_id: String) = cardsService.find(card_id)

    @GetMapping("/{id}/orders")
    fun getOrders(@PathVariable("id") card_id: String) = cardsService.getOrders(card_id)
}
