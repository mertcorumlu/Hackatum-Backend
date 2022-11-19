package de.tum.hack.Bloomberg.Challenge.services

import de.tum.hack.Bloomberg.Challenge.api.OrderTO
import de.tum.hack.Bloomberg.Challenge.models.*
import de.tum.hack.Bloomberg.Challenge.repositories.MasterOrderRepository
import de.tum.hack.Bloomberg.Challenge.repositories.MatchRepository
import de.tum.hack.Bloomberg.Challenge.repositories.SnapshotOrderRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
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
        val master = getMaster(user, card, side = order.side)

        if (master != null) { return master to true }

        val masterEntity = MasterOrder(
            card = card,
            user = user,
            quantity = order.quantity,
            price = order.price,
            side = order.side
        )

        val snapEntity = SnapshotOrder(
            masterOrder = masterEntity,
            quantity = order.quantity
        )

        snapshots.saveAndFlush(snapEntity)
        return masters.saveAndFlush(masterEntity) to false
    }

    @Transactional
    fun add(user: User, card: Card, order: OrderTO) {
        val pair = createIfNull(user, card, order)

        if (!pair.second) { return }

        // first increase
        val master = pair.first
        master.quantity += order.quantity
        master.snapshotOrder?.also{
            it.quantity += order.quantity
            snapshots.saveAndFlush(it)
        }
        masters.saveAndFlush(master)

       findMatching(master)
        return
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
                masters.saveAndFlush(this)
                snapshots.delete(it)

                return false.xor(reverse)
            }

            snapshots.saveAndFlush(it)
        }

        seller.quantity -= quant

        if (seller.quantity == 0) {
            seller.masterOrder.completed = true
            masters.saveAndFlush(seller.masterOrder)
            snapshots.delete(seller)
            return true.xor(reverse)
        }

        snapshots.saveAndFlush(seller)
        return true.xor(reverse)
    }

}
