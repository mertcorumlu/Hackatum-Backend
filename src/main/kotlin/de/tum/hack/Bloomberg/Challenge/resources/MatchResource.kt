package de.tum.hack.Bloomberg.Challenge.resources

import de.tum.hack.Bloomberg.Challenge.models.Match
import de.tum.hack.Bloomberg.Challenge.models.Side
import de.tum.hack.Bloomberg.Challenge.services.MatchService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/matches")
class MatchResource(
    val matchService: MatchService
) {

    @GetMapping
    fun filterMatches(
        @RequestParam("userId", required = false) userId: Int? = null,
        @RequestParam("cardId", required = false) cardId: String? = null,
        @RequestParam("side", required = false) side: Side? = null
    ): List<Match> {
        return matchService.search(userId, cardId, side)
    }

}
