package de.tum.hack.bloomberg.challenge.resources

import de.tum.hack.Bloomberg.Challenge.api.HttpResponse
import de.tum.hack.Bloomberg.Challenge.services.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("api/user")
class UserResource(
    val userService: UserService
) {

    @PostMapping("sign_in")
    fun signin(
        @RequestParam name: String,
        @RequestParam password: String
    ): HttpResponse = userService.signin(name, password)

}
