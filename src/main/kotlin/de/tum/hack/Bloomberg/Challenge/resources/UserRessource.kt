package de.tum.hack.Bloomberg.Challenge.resources

import de.tum.hack.Bloomberg.Challenge.api.LoginPayload
import de.tum.hack.Bloomberg.Challenge.models.User
import de.tum.hack.Bloomberg.Challenge.services.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController("/api")
class UserRessource(val userService: UserService) {

    @PostMapping("/sign-in")
    fun signIn(@RequestBody body: LoginPayload): User {
        return userService.signIn(body.username, body.password)
    }

    @PostMapping("/sign-up")
    fun signUp(@RequestBody body: LoginPayload): User {
        return userService.signUp(body.username, body.password)
    }
}