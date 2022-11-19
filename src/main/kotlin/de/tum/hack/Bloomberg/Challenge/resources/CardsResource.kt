package de.tum.hack.Bloomberg.Challenge.resources

import de.tum.hack.Bloomberg.Challenge.models.Card
import de.tum.hack.Bloomberg.Challenge.services.CardsService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/cards")
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

    @PostMapping
    fun addCard(@RequestBody card: Card) = cardsService.add(card)
}
