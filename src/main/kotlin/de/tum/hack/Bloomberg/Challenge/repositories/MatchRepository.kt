package de.tum.hack.Bloomberg.Challenge.repositories;

import de.tum.hack.Bloomberg.Challenge.models.Match
import de.tum.hack.Bloomberg.Challenge.models.Side
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MatchRepository : JpaRepository<Match, Int> {

        @Query("""
            select m from Match m
            where m.buyer.user.id = :userId or m.seller.user.id = :userId
            order by m.price
        """)
        fun filterMatchesByUserId(userId: Int): List<Match>

        @Query("""
            select m from Match m
            where m.buyer.card.cardId = :cardId
            order by m.price
        """)
        fun filterMatchesByCardId(cardId: String): List<Match>

        fun filterMatchesBySide(side: Side): List<Match>
}
