package de.tum.hack.Bloomberg.Challenge.resources

import de.tum.hack.Bloomberg.Challenge.api.LoginPayload
import de.tum.hack.Bloomberg.Challenge.api.SortTypes
import de.tum.hack.Bloomberg.Challenge.models.User
import de.tum.hack.Bloomberg.Challenge.services.UserService
import org.springframework.web.bind.annotation.*

@RestController()
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
    fun filterCards(@PathVariable("user_id") userId: Int, @RequestParam("sortBy") sortBy: SortTypes) = userService.sortCards(userId, sortBy)

    @GetMapping("/{user_id}/top_cards")
    fun getTopCards(@PathVariable("user_id") userId: Int) = userService.getTop(userId)
}
