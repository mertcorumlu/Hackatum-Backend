package de.tum.hack.Bloomberg.Challenge.resources

import de.tum.hack.Bloomberg.Challenge.api.OrderTO
import de.tum.hack.Bloomberg.Challenge.services.CardsService
import de.tum.hack.Bloomberg.Challenge.services.OrderService
import de.tum.hack.Bloomberg.Challenge.services.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/order")
class OrderResource(
    val userService: UserService,
    val cardsService: CardsService,
    val ordersService: OrderService
) {

    @PostMapping("/add")
    fun addOrder(@RequestBody order: OrderTO) {
        val user = userService.signIn(order.username, order.password)
        val card = cardsService.find(order.cardId)

        ordersService.add(user, card, order)
    }

    @PostMapping("/del")
    fun delOrder(@RequestBody order: OrderTO) {
        val user = userService.signIn(order.username, order.password)
        val card = cardsService.find(order.cardId)

        ordersService.del(user, card, order)
    }
}
