package de.tum.hack.Bloomberg.Challenge.services

import de.tum.hack.Bloomberg.Challenge.api.FilterSnapshotsResponse
import de.tum.hack.Bloomberg.Challenge.api.OrderTO
import de.tum.hack.Bloomberg.Challenge.models.*
import de.tum.hack.Bloomberg.Challenge.repositories.MasterOrderRepository
import de.tum.hack.Bloomberg.Challenge.repositories.MatchRepository
import de.tum.hack.Bloomberg.Challenge.repositories.SnapshotOrderRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import kotlin.math.min

@Service
class OrderService(
    var matches: MatchRepository,
    val masters: MasterOrderRepository,
    val snapshots: SnapshotOrderRepository
) {

    fun getMaster(user: User, card: Card, side: Side) =
        masters.findAllByUserAndCardAndCompletedIsFalseAndSide(user, card, side).firstOrNull()

    fun createIfNull(user: User, card: Card, order: OrderTO): Pair<MasterOrder, Boolean> {
        var master = getMaster(user, card, side = order.side)

        if (master != null) { return master to true }

        val masterEntity = MasterOrder(
            card = card,
            user = user,
            quantity = order.quantity,
            price = order.price,
            side = order.side,
            updated = LocalDateTime.now()
        )

        master = masters.saveAndFlush(masterEntity)

        var snapEntity = SnapshotOrder(
            masterOrder = master,
            quantity = order.quantity
        )

        snapEntity = snapshots.saveAndFlush(snapEntity)

        masterEntity.snapshotOrder = snapEntity
        master = masters.saveAndFlush(master)

        return master to false
    }

    @Transactional
    fun add(user: User, card: Card, order: OrderTO) {
        val pair = createIfNull(user, card, order)

        val master = pair.first

        if (pair.second) {
            master.quantity += order.quantity
            master.updated = LocalDateTime.now()
            master.snapshotOrder?.also{
                it.quantity += order.quantity
                snapshots.saveAndFlush(it)
            }
            masters.saveAndFlush(master)
        }

        findMatching(master)
    }

    @Transactional
    fun del(user: User, card: Card, order: OrderTO) {
        val master = getMaster(user, card, side = order.side)
        val snapshot = master?.snapshotOrder ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST)

        snapshot.quantity -= order.quantity

        if (snapshot.quantity < 0) { throw ResponseStatusException(HttpStatus.BAD_REQUEST) }

        if (snapshot.quantity == 0) {
            master.completed = true
            masters.saveAndFlush(master)
            snapshots.delete(snapshot)
            return
        }

        master.quantity -= order.quantity

        snapshots.saveAndFlush(snapshot)
        masters.saveAndFlush(master)
    }

    private fun findMatching(masterOrder: MasterOrder) {
        if (masterOrder.side == Side.BUY) {
            val sellers = snapshots.findSellersForBuyer(masterOrder.card.cardId, masterOrder.user.id!!, masterOrder.price)

            sellers.forEach { seller ->
                if(!masterOrder.buy(seller, false))
                    return@forEach
            }
        } else {
            val buyers = snapshots.findBuyersForSeller(masterOrder.card.cardId, masterOrder.user.id!!, masterOrder.price)

            buyers.forEach { buyer ->
                if(!buyer.masterOrder.buy(masterOrder.snapshotOrder!!, true))
                    return@forEach
            }
        }
    }

    private fun MasterOrder.buy(seller: SnapshotOrder, reverse: Boolean): Boolean {

        var ret = true.xor(reverse)

        val quant = min(seller.quantity, this.quantity)

        val match = Match(
            buyer = this,
            seller = seller.masterOrder,
            quantity = quant,
            price = seller.masterOrder.price
        )
        matches.saveAndFlush(match)

        this.snapshotOrder?.also {
            it.quantity -= quant

            if (it.quantity == 0) {
                this.completed = true
                this.updated = LocalDateTime.now()
                masters.saveAndFlush(this)
                snapshots.delete(it)

                ret = false.xor(reverse)
            } else {
                snapshots.saveAndFlush(it)
            }
        }

        seller.quantity -= quant

        if (seller.quantity == 0) {
            seller.masterOrder.completed = true
            seller.masterOrder.updated = LocalDateTime.now()
            masters.saveAndFlush(seller.masterOrder)
            snapshots.delete(seller)
        } else {
            snapshots.saveAndFlush(seller)
        }

        return ret
    }

//    fun filterSnapshotsByBuy(): FilterSnapshotsResponse {
//        return snapshots.filterSnapshotsByBuy()
//    }
//
//    fun filterSnapshotsBySell(): FilterSnapshotsResponse {
//        return snapshots.filterSnapshotsBySell()
//    }
//
//    fun filterSnapshotsByCardId(cardId: String): FilterSnapshotsResponse {
//        return snapshots.filterSnapshotsByCardId(cardId)
//    }
//
//    fun filterSnapshotsByUsername(username: String): FilterSnapshotsResponse {
//        return snapshots.filterSnapshotsByUsername(username)
//    }

}
