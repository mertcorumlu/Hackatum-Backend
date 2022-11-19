package de.tum.hack.Bloomberg.Challenge.resources

import de.tum.hack.Bloomberg.Challenge.models.MasterOrder
import de.tum.hack.Bloomberg.Challenge.models.Side
import de.tum.hack.Bloomberg.Challenge.models.SnapshotOrder
import de.tum.hack.Bloomberg.Challenge.services.OrderService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/snapshot")
class SnapshotResource(val orderService: OrderService) {

    @GetMapping
    fun filterSnapshot(
        @RequestParam("side", required = false) side: Side? = null,
        @RequestParam("userId", required = false) userId: Int? = null,
        @RequestParam("cardId", required = false) cardId: String? = null): List<MasterOrder> {
        return orderService.search(side, userId, cardId)
    }
}
