package de.tum.hack.Bloomberg.Challenge.services

import de.tum.hack.Bloomberg.Challenge.api.HttpResponse
import de.tum.hack.Bloomberg.Challenge.api.ResponseType
import de.tum.hack.Bloomberg.Challenge.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository
) {

    fun signin(name: String, password: String): HttpResponse {
        return HttpResponse(ResponseType.SUCCESS)
    }

}
