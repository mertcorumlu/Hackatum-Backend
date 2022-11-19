package de.tum.hack.Bloomberg.Challenge.repositories;

import de.tum.hack.Bloomberg.Challenge.models.SnapshotOrder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SnapshotOrderRepository : JpaRepository<SnapshotOrder, Int> {

    @Query("""
        select s from SnapshotOrder s 
        where s.masterOrder.card.cardId = :cardId and s.masterOrder.user.id <> :user_id and
        s.masterOrder.price >= :price order by s.masterOrder.price
        """)
    fun findBuyersForSeller(cardId: String, user_id: Int, price: Double): List<SnapshotOrder>

    @Query("""
        select s from SnapshotOrder s 
        where s.masterOrder.card.cardId = :cardId and s.masterOrder.user.id <> :user_id and
        s.masterOrder.price <= :price order by s.masterOrder.price desc
        """)
    fun findSellersForBuyer(cardId: String, user_id: Int, price: Double): List<SnapshotOrder>
}
