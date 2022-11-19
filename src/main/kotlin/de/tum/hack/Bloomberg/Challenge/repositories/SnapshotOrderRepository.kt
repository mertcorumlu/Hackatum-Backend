package de.tum.hack.Bloomberg.Challenge.repositories;

import de.tum.hack.Bloomberg.Challenge.api.FilterSnapshotsResponse
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

    fun findAllById(userId: Int): List<SnapshotOrder>

//    @Query("""
//        select * from snapshotorders s
//        join masterorders m on s.master_order_id = m.id
//        where m.side = false
//        order by m.price
//        """, nativeQuery = true)
//    fun filterSnapshotsByBuy(): FilterSnapshotsResponse
//
//    @Query("""
//        select * from snapshotorders s
//        join masterorders m on s.master_order_id = m.id
//        where m.side = true
//        order by m.price
//        """, nativeQuery = true)
//    fun filterSnapshotsBySell(): FilterSnapshotsResponse
//
//    @Query("""
//        select * from snapshotorders s
//        join masterorders m on s.master_order_id = m.id
//        where m.card_id = :cardId
//        order by m.price
//        """, nativeQuery = true)
//    fun filterSnapshotsByCardId(cardId: String): FilterSnapshotsResponse
//
//    @Query("""
//        select * from snapshotorders s
//        join masterorders m, users u on s.master_order_id = m.id and m.user_id = users.id
//        where u.username = :username
//        order by m.price
//        """, nativeQuery = true)
//    fun filterSnapshotsByUsername(username: String): FilterSnapshotsResponse

    @Query("""
        select s from SnapshotOrder s 
        where s.masterOrder.card.cardId = :cardId and s.masterOrder.user.id <> :user_id and
        s.masterOrder.price <= :price order by s.masterOrder.price desc
        """)
    fun findSellersForBuyer(cardId: String, user_id: Int, price: Double): List<SnapshotOrder>
}
