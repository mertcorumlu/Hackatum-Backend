package de.tum.hack.Bloomberg.Challenge.resources

import de.tum.hack.Bloomberg.Challenge.api.FilterTypes
import de.tum.hack.Bloomberg.Challenge.api.OrderTO
import de.tum.hack.Bloomberg.Challenge.models.Side
import de.tum.hack.Bloomberg.Challenge.models.SnapshotOrder
import de.tum.hack.Bloomberg.Challenge.repositories.SnapshotOrderRepository
import de.tum.hack.Bloomberg.Challenge.services.CardsService
import de.tum.hack.Bloomberg.Challenge.services.OrderService
import de.tum.hack.Bloomberg.Challenge.services.UserService
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/order")
class OrderResource(
    val userService: UserService,
    val cardsService: CardsService,
    val ordersService: OrderService,
    val snapshots: SnapshotOrderRepository
) {

    @PostMapping("/")
    fun addOrder(@RequestBody order: OrderTO) {
        val user = userService.signIn(order.username, order.password)
        val card = cardsService.find(order.cardId)

        ordersService.add(user, card, order)
    }

    @DeleteMapping("/")
    fun delOrder(@RequestBody order: OrderTO) {
        val user = userService.signIn(order.username, order.password)
        val card = cardsService.find(order.cardId)

        ordersService.del(user, card, order)
    }

    @GetMapping("/")
    fun filterSnapshots(@RequestParam("filterBy") filterBy: FilterTypes, @RequestParam("userId") userId: Int? = null, @RequestParam("cardId") cardId: String? = null): List<SnapshotOrder> {
        when (filterBy) {
            FilterTypes.BUY -> return snapshots.filterSnapshotsBySide(Side.BUY)
            FilterTypes.SELL -> return snapshots.filterSnapshotsBySide(Side.SELL)
            FilterTypes.USERNAME -> {
                userId?.also {
                    return snapshots.filterSnapshotsByUserId(it)
                } ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "missing param")
            }
            FilterTypes.CARDID -> {
                cardId?.also {
                    return snapshots.filterSnapshotsByCardId(it)
                } ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "missing param")
            }
        }
        throw ResponseStatusException(HttpStatus.NOT_FOUND, "no such filter")
    }
}
