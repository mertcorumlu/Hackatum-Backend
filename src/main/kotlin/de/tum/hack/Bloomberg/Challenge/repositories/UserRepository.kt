package de.tum.hack.Bloomberg.Challenge.repositories;

import de.tum.hack.Bloomberg.Challenge.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Int> {

    fun findAllByUsernameEquealsAndPasswordEquals(username: String, password: String): User?

    fun findByUsername(username: String): User?
}
