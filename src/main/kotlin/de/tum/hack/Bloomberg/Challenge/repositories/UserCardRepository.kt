package de.tum.hack.Bloomberg.Challenge.repositories;

import de.tum.hack.Bloomberg.Challenge.models.Card
import de.tum.hack.Bloomberg.Challenge.models.User
import de.tum.hack.Bloomberg.Challenge.models.UserCard
import org.springframework.data.jpa.repository.JpaRepository

interface UserCardRepository : JpaRepository<UserCard, Int> {
    fun findAllByUserId(userId: Int): List<UserCard>
    fun findFirstByUserAndCard(user: User, card: Card): UserCard?
    fun findFirstByUserIdAndCardId(userId: Int, cardId: String): UserCard?
}
