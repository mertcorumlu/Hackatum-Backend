package de.tum.hack.Bloomberg.Challenge.resources

import de.tum.hack.Bloomberg.Challenge.api.SortTypes
import de.tum.hack.Bloomberg.Challenge.services.UserCardService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("/api/users")
class UserCardRessource(val userCardService: UserCardService) {

    @GetMapping("/{user_id}/cards")
    fun filterCards(@PathVariable("user_id") userId: Int, @RequestParam("sortBy") sortBy: SortTypes) = userCardService.sortCards(userId, sortBy)

    @GetMapping("/{user_id}/top_cards")
    fun getTopCards(@PathVariable("user_id") userId: Int) = userCardService.getTop(userId)
}