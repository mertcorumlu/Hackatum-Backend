package de.tum.hack.Bloomberg.Challenge.repositories;

import de.tum.hack.Bloomberg.Challenge.models.Card
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CardRepository : JpaRepository<Card, Int> {
    fun findByCardId(card_id: String): Card?

    @Query("""
        select * from user_cards u
        join cards c on c.card_id = u.card_id
        where u.user_id = :user_id
        order by c.name
        limit 5
        """, nativeQuery = true)
    fun findTop5ByCardIdInListOrderByNameAsc(user_id: Int): List<Card>

    @Query("""
        select * from user_cards u
        join cards c on c.card_id = u.card_id
        where u.user_id = :user_id
        order by c.name
        """, nativeQuery = true)
    fun findAllByCardIdInListOrderByNameAsc(user_id: Int): List<Card>

    @Query("""
        select u from UserCard u
        join Card c on c.cardId = u.card.cardId
        where u.user.id = :user_id
        order by c.rarity
    """)
    fun findAllByCardIdInListOrderByRarity(user_id: Int): List<Card>

//    fun findAllByCardIdInListOrderByVaue
}
