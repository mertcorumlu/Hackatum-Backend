package de.tum.hack.Bloomberg.Challenge.repositories;

import de.tum.hack.Bloomberg.Challenge.models.UserCard
import org.springframework.data.jpa.repository.JpaRepository

interface UserCardRepository : JpaRepository<UserCard, Int> {
    fun findAllByUserId(userId: Int): List<UserCard>
}
