package de.tum.hack.Bloomberg.Challenge.resources

import de.tum.hack.Bloomberg.Challenge.api.LoginPayload
import de.tum.hack.Bloomberg.Challenge.api.SortTypes
import de.tum.hack.Bloomberg.Challenge.models.User
import de.tum.hack.Bloomberg.Challenge.services.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserResource(val userService: UserService) {

    @PostMapping("/sign-in")
    fun signIn(@RequestBody body: LoginPayload): User {
        return userService.signIn(body.username, body.password)
    }

    @PostMapping("/sign-up")
    fun signUp(@RequestBody body: LoginPayload): User {
        return userService.signUp(body.username, body.password)
    }

    @GetMapping("/{user_id}/cards")
    fun getUserCards(
        @PathVariable("user_id") userId: Int,
        @RequestParam("sortBy") sortBy: SortTypes
    ) = userService.sortCards(userId, sortBy)

    @GetMapping("/{user_id}/card/{card_id}/count")
    fun getUserCardCount(
        @PathVariable("user_id") userId: Int,
        @PathVariable("card_id") cardId: String
    ) = userService.getUserCardCount(userId, cardId)

    @GetMapping("/{user_id}/top3_cards")
    fun getUsersTop3CardsWithPrices(@PathVariable("user_id") userId: Int) = userService.getTop3(userId)

    @GetMapping("/{user_id}/cards_prices")
    fun getUserCardsWithPrices(@PathVariable("user_id") userId: Int) = userService.getTop(userId)
}
