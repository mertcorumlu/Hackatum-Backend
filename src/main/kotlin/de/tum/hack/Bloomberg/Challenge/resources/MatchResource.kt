package de.tum.hack.Bloomberg.Challenge.resources

import de.tum.hack.Bloomberg.Challenge.api.FilterTypes
import de.tum.hack.Bloomberg.Challenge.models.Match
import de.tum.hack.Bloomberg.Challenge.repositories.MatchRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/matches")
class MatchResource(
    val matchRepository: MatchRepository
) {

    @GetMapping("/")
    fun filterMatches(
        @RequestParam("filterBy", required = false) filterBy: FilterTypes? = null,
        @RequestParam("userId", required = false) userId: Int? = null,
        @RequestParam("cardId", required = false) cardId: String? = null
    ): List<Match> {
        return when (filterBy) {
            FilterTypes.USERID -> {
                userId?.let {
                    matchRepository.filterMatchesByUserId(it)
                } ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "missing param")
            }
            FilterTypes.CARDID -> {
                cardId?.let {
                    matchRepository.filterMatchesByCardId(it)
                } ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "missing param")
            }
            else -> {
                matchRepository.findAll()
            }
        }
    }

}
