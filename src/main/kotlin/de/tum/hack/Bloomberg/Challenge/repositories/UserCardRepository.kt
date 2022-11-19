package de.tum.hack.Bloomberg.Challenge.repositories;

import de.tum.hack.Bloomberg.Challenge.models.UserCard
import org.springframework.data.jpa.repository.JpaRepository

interface UserCardRepository : JpaRepository<UserCard, String> {
    fun findAllCardByUserId(userId: Int): List<UserCard>
}
