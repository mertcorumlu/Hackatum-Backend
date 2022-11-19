package de.tum.hack.Bloomberg.Challenge.resources

import de.tum.hack.Bloomberg.Challenge.api.FilterSnapshotsResponse
import de.tum.hack.Bloomberg.Challenge.api.FilterTypes
import de.tum.hack.Bloomberg.Challenge.api.OrderTO
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
    fun filterSnapshots(@RequestParam("filterBy") filterBy: FilterTypes, @RequestParam("username") username: String? = null, @RequestParam("cardId") cardId: String? = null): FilterSnapshotsResponse {
        when (filterBy) {
            FilterTypes.BUY -> return ordersService.filterSnapshotsByBuy()
            FilterTypes.SELL -> return ordersService.filterSnapshotsBySell()
            FilterTypes.USERNAME -> {
                username?.also {
                    return ordersService.filterSnapshotsByUsername(it)
                } ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "missing param")
            }
            FilterTypes.CARDID -> {
                cardId?.also {
                    return ordersService.filterSnapshotsByCardId(it)
                } ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "missing param")
            }
        }
        throw ResponseStatusException(HttpStatus.NOT_FOUND, "no such filter")
    }
}
