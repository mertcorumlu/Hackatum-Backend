package de.tum.hack.Bloomberg.Challenge.repositories;

import de.tum.hack.Bloomberg.Challenge.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Int> {

    fun findByNameAndPassword(name: String, password: String): User?

    fun findByName(username: String): User?

}
