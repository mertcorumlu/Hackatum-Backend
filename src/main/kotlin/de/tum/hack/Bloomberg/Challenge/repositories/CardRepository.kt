package de.tum.hack.Bloomberg.Challenge.repositories;

import de.tum.hack.Bloomberg.Challenge.models.Card
import de.tum.hack.Bloomberg.Challenge.models.UserCard
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CardRepository : JpaRepository<Card, Int> {
    fun findByCardId(card_id: String): Card?

    @Query("""
        select u from UserCard u where u.user.id = :user_id
        order by u.card.name asc
        """)
    fun findAllByUserIdOrderByCardName(user_id: Int): List<UserCard>

    @Query("""
        select u from UserCard u where u.user.id = :user_id
        order by u.card.rarity
    """)
    fun findAllByUserOrderByCardRarity(user_id: Int): List<UserCard>
}
