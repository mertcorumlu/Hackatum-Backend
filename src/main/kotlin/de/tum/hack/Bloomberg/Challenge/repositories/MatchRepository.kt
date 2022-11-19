package de.tum.hack.Bloomberg.Challenge.repositories;

import de.tum.hack.Bloomberg.Challenge.api.FilterMatchesResponse
import de.tum.hack.Bloomberg.Challenge.models.Match
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MatchRepository : JpaRepository<Match, Int> {
    @Query("""
        select m from matches m
        where m.buyer.side = false and m.seller.side = false
        order by m.price
        """)
    fun filterMatchesByBuy(): FilterMatchesResponse

    @Query("""
        select m from matches m
        join master_orders x1 on x1.id = m.buying_order_id join master_orders x2 on x2.id = m.selling_order_id 
        where m.side = true
        order by x1.price
        """, nativeQuery = true)
    fun filterMatchesBySell(): FilterMatchesResponse

    @Query("""
        select * from matches m from matches m 
        join master_orders x1 on x1.id = m.buying_order_id join master_orders x2 on x2.id = m.selling_order_id 
        where m.card_id = :cardId
        order by x1.price
        """, nativeQuery = true)
    fun filterMatchesByCardId(cardId: String): FilterMatchesResponse

    @Query("""
        select * from matches m
        join master_orders x1 on x1.id = m.buying_order_id join master_orders x2 on x2.id = m.selling_order_id join user u
        where (u.id = x1.id or u.id = x2.id) and u.name = :username
        order by x1.price
        """, nativeQuery = true)
    fun filterMatchesByUsername(username: String): FilterMatchesResponse
}
