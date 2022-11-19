package de.tum.hack.Bloomberg.Challenge.resources

import de.tum.hack.Bloomberg.Challenge.api.FilterMatchesResponse
import de.tum.hack.Bloomberg.Challenge.api.FilterSnapshotsResponse
import de.tum.hack.Bloomberg.Challenge.api.FilterTypes
import de.tum.hack.Bloomberg.Challenge.services.MatchService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/matches")
class MatchRessource(
        val matchService: MatchService
) {

    @GetMapping("/")
    fun filterSnapshots(@RequestParam("filterBy") filterBy: FilterTypes, @RequestParam("username") username: String? = null, @RequestParam("cardId") cardId: String? = null): FilterMatchesResponse {
        when (filterBy) {
            FilterTypes.BUY -> return matchService.filterMatchesByBuy()
            FilterTypes.SELL -> return matchService.filterMatchesBySell()
            FilterTypes.USERNAME -> {
                username?.also {
                    return matchService.filterMatchesByUsername(it)
                } ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "missing param")
            }
            FilterTypes.CARDID -> {
                cardId?.also {
                    return matchService.filterMatchesByCardId(it)
                } ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "missing param")
            }
        }
        throw ResponseStatusException(HttpStatus.NOT_FOUND, "no such filter")
    }

}