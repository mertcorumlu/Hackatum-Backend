package de.tum.hack.Bloomberg.Challenge.services

import de.tum.hack.Bloomberg.Challenge.models.Match
import de.tum.hack.Bloomberg.Challenge.models.Side
import de.tum.hack.Bloomberg.Challenge.repositories.MatchRepository
import org.springframework.stereotype.Service

@Service
class MatchService(val matchRepository: MatchRepository) {

    fun search(userId: Int?, cardId: String?, side: Side?): List<Match> {
        return matchRepository.findAll().mapNotNull { match ->
            userId?.also {
                if (match.buyer.user.id != it && match.seller.user.id != it) {
                    return@mapNotNull null
                }
            }

            cardId?.also {
                if (match.seller.card.cardId != it) {
                    return@mapNotNull null
                }
            }

            side?.also {
                if (match.seller.side != side) {
                    return@mapNotNull null
                }
            }

            match.seller.username = match.seller.user.name
            match.buyer.username = match.buyer.user.name
            match
        }.sortedBy { it.created }.reversed()
    }

}
