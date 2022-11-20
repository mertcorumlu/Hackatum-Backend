package de.tum.hack.Bloomberg.Challenge.services

import de.tum.hack.Bloomberg.Challenge.api.BuySellOrdersResponse
import de.tum.hack.Bloomberg.Challenge.api.Price
import de.tum.hack.Bloomberg.Challenge.api.PlotResponse
import de.tum.hack.Bloomberg.Challenge.models.Card
import de.tum.hack.Bloomberg.Challenge.models.MasterOrder
import de.tum.hack.Bloomberg.Challenge.models.Side
import de.tum.hack.Bloomberg.Challenge.repositories.CardRepository
import de.tum.hack.Bloomberg.Challenge.repositories.MasterOrderRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.Duration
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Root
import kotlin.math.pow


@Service
class CardsService(
    val cardRepository: CardRepository,
    val masterOrderRepository: MasterOrderRepository
) {

    @PersistenceContext
    val em: EntityManager? = null

    fun search(q: String?, sortBy: String?): List<Card> {

        em?.also { em ->
            val cb: CriteriaBuilder = em.criteriaBuilder
            val cr = cb.createQuery(Card::class.java)
            val root: Root<Card> = cr.from(Card::class.java)

            cr.select(root)

            q?.also {
                cr.where(cb.like(root.get("name"), "%${q}%"))
            }

            sortBy?.also {
                cr.orderBy(cb.asc(root.get<Any>(sortBy)))
            }

            return em.createQuery(cr).resultList
        }

        return emptyList()
    }

    fun find(card_id: String): Card {
        return cardRepository.findByCardId(card_id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun getPrice(card_id: String): Price {
        val card = find(card_id)
        return Price(
            price = masterOrderRepository.findAllByCompletedIsFalseAndSideAndCard(Side.SELL, card).minByOrNull { it.price }?.price
        )
    }

    fun getOrders(card_id: String): BuySellOrdersResponse {

        val card = find(card_id)
        val buy: MutableList<MasterOrder> = mutableListOf()
        val sell: MutableList<MasterOrder> = mutableListOf()

        card.masterOrders.forEach {  master ->

            master.snapshotOrder?.also { snapshot ->
                master.quantity = snapshot.quantity
                master.username = master.user.name
                if (master.side == Side.BUY) {
                    buy.add(master)
                } else {
                    sell.add(master)
                }
            }

        }

        return BuySellOrdersResponse(buy, sell)
    }

    fun add(card: Card) {
        cardRepository.save(card)
    }

    fun getTransactionHistory(cardId: String): List<MasterOrder> {
        return cardRepository.findAllTransactionsOfCard(cardId)
    }

    fun buildRegressionModelResponse(cardId: String, days: Int?): PlotResponse? {
        val data = getTransactionHistory(cardId)
        val xs = mutableListOf<Int>()
        val ys = mutableListOf<Double>()

        var firstDate: LocalDateTime
        try {
            firstDate = data.first().updated ?: return null
        } catch (e: NoSuchElementException) {
            return null
        }

        data.forEach { entry ->
            xs.add(Duration.between(firstDate, entry.updated).toDays().toInt())
            ys.add(entry.price)
        }

        val variance = xs.sumOf { x -> (x - xs.average()).pow(2) }

        val covariance = xs.zip(ys) { x, y -> (x - xs.average()) * (y - ys.average())}.sum()

        val slope = covariance / variance

        val yIntercept = ys.average() - slope * xs.average()

        val lg = { independentVariable: Int -> slope * independentVariable + yIntercept }

        return generatePlotHistory(lg, data, firstDate, days)
    }

    fun generatePlotHistory(lg: (Int) -> Double, data: List<MasterOrder>, startingDate: LocalDateTime, days: Int?): PlotResponse {
        val xs = mutableListOf<Int>()
        val ys = mutableListOf<Double>()
        val vals = mutableListOf<Double>()
        data.forEach() {
            val x = Duration.between(startingDate, it.updated).toDays().toInt()
            xs.add(x)
            ys.add(lg(x))
            vals.add(it.price)
        }

        days ?: return PlotResponse(xs, ys, vals)

        var last: Int
        try {
            last = xs.last()
        } catch (e: NoSuchElementException) {
            return PlotResponse(xs, ys, vals)
        }
        ++last
        for (i in 0 until days) {
            xs.add(last)
            ys.add(lg(last))
            ++last
        }

        return PlotResponse(xs, ys, vals)
    }
}
