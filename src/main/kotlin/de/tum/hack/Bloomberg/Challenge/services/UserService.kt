package de.tum.hack.Bloomberg.Challenge.services

import de.tum.hack.Bloomberg.Challenge.models.SnapshotOrder
import de.tum.hack.Bloomberg.Challenge.models.User
import de.tum.hack.Bloomberg.Challenge.repositories.SnapshotOrderRepository
import de.tum.hack.Bloomberg.Challenge.repositories.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime


@Service
class UserService(
        val userRepository: UserRepository,
        val snapshotOrderRepository: SnapshotOrderRepository) {
    fun signIn(username: String, password: String): User {
        return userRepository.findByNameAndPassword(username, password)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No user found")
    }

    fun signUp(username: String, password: String): User {
        val usernameExists = (userRepository.findByName(username) != null)
        if (usernameExists) {
            throw ResponseStatusException(HttpStatus.FOUND, "Username already exists")
        }
        val user = User(name = username, password = password, registered = LocalDateTime.now(), balance = 5000.0)
        return userRepository.save(user)
    }

    fun getSnapshots(user_id: Int): List<SnapshotOrder> {
        return snapshotOrderRepository.findAllById(user_id)
    }
}
