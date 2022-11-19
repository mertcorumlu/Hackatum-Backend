package de.tum.hack.Bloomberg.Challenge.services

import de.tum.hack.Bloomberg.Challenge.api.OrderTO
import de.tum.hack.Bloomberg.Challenge.models.*
import de.tum.hack.Bloomberg.Challenge.repositories.*
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import kotlin.math.min

@Service
class OrderService(
    var matches: MatchRepository,
    val masters: MasterOrderRepository,
    val snapshots: SnapshotOrderRepository,
    val userRepository: UserRepository,
    val userCardRepository: UserCardRepository
) {

    @PersistenceContext
    val em: EntityManager? = null

    fun search(side: Side?, userId: Int?, cardId: String?): List<SnapshotOrder> {

        em?.also { em ->
            val cb: CriteriaBuilder = em.criteriaBuilder
            val cr = cb.createQuery(MasterOrder::class.java)
            val root: Root<MasterOrder> = cr.from(MasterOrder::class.java)

            val preds: MutableList<Predicate> = mutableListOf()

            side?.also {
                preds.add(cb.equal(root.get<Side>("side"), it))
            }

            userId?.also {
                preds.add(cb.equal(root.get<Int>("user_id"), it))
            }

            cardId?.also {
                preds.add(cb.equal(root.get<String>("card_id"), it))
            }

            cr.select(root)
            cr.where(cb.and(*preds.toTypedArray()))

            return em.createQuery(cr).resultList.mapNotNull { it.snapshotOrder }
        }

        return emptyList()
    }

    fun getMaster(user: User, card: Card, side: Side, price: Double) =
        masters.findAllByUserAndCardAndCompletedIsFalseAndSideAndPrice(user, card, side, price).firstOrNull()

    fun createIfNull(user: User, card: Card, order: OrderTO): Pair<MasterOrder, Boolean> {
        var master = getMaster(user, card, side = order.side, price = order.price)

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

        if (user.balance < order.quantity * order.price)
            throw ResponseStatusException(HttpStatus.NOT_ACCEPTABLE)

        val pair = createIfNull(user, card, order)

        val master = pair.first

        master.user.balance -= order.price * order.quantity

        userRepository.saveAndFlush(master.user)

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
        val master = getMaster(user, card, side = order.side, price = order.price)
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
        master.user.balance += order.price * order.price

        userRepository.saveAndFlush(master.user)
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
        val price = seller.masterOrder.price

        // balance update
        seller.masterOrder.user.balance += quant * price
//        this.user.balance -= quant * price

        userRepository.save(seller.masterOrder.user)
//        userRepository.save(this.user)

        val card = this.card

        var buyerCard = userCardRepository.findFirstByUserAndCard(this.user, card)
        val sellerCard = userCardRepository.findFirstByUserAndCard(seller.masterOrder.user, card)

        if (sellerCard == null || sellerCard.count < quant)
            throw ResponseStatusException(HttpStatus.NOT_ACCEPTABLE)

        sellerCard.count -= quant

        if (sellerCard.count == 0) {
            userCardRepository.delete(sellerCard)
        } else {
            userCardRepository.save(sellerCard)
        }

        if (buyerCard == null) {
            buyerCard = UserCard(user = this.user, card = card, count = quant)
        } else {
            buyerCard.count += quant
        }

        userCardRepository.save(buyerCard)

        val match = Match(
            buyer = this,
            seller = seller.masterOrder,
            quantity = quant,
            price = price
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

}
