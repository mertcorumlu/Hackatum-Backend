package de.tum.hack.Bloomberg.Challenge.services

import de.tum.hack.Bloomberg.Challenge.api.FilterSortUserCardResponse
import de.tum.hack.Bloomberg.Challenge.api.SortTypes
import de.tum.hack.Bloomberg.Challenge.models.Card
import de.tum.hack.Bloomberg.Challenge.models.Side
import de.tum.hack.Bloomberg.Challenge.models.User
import de.tum.hack.Bloomberg.Challenge.models.UserCard
import de.tum.hack.Bloomberg.Challenge.repositories.CardRepository
import de.tum.hack.Bloomberg.Challenge.repositories.MasterOrderRepository
import de.tum.hack.Bloomberg.Challenge.repositories.UserCardRepository
import de.tum.hack.Bloomberg.Challenge.repositories.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import kotlin.math.min


@Service
class UserService(
    val userRepository: UserRepository,
    val cardRepository: CardRepository,
    val masterOrderRepository: MasterOrderRepository,
    val userCardRepository: UserCardRepository
    ) {
    fun signIn(username: String, password: String): User {
        return userRepository.findByNameAndPassword(username, password)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No user found")
    }

    fun signUp(username: String, password: String): User {
        val usernameExists = (userRepository.findByName(username) != null)
        if (usernameExists) {
            throw ResponseStatusException(HttpStatus.FOUND, "Username already exists")
        }
        val user = User(name = username, password = password, registered = LocalDateTime.now(), balance = 5000.0)
        return userRepository.save(user)
    }

    fun sortCards(userId: Int, sortBy: SortTypes): List<UserCard> {
        return when (sortBy) {
            SortTypes.RARITY -> cardRepository.findAllByUserOrderByCardRarity(userId)
            else -> cardRepository.findAllByUserIdOrderByCardName(userId)
        }
    }

    fun getTop(userId: Int): List<Pair<UserCard, Double>> {
        val userCards = userCardRepository.findAllByUserId(userId)

        val list = userCards.mapNotNull { userCard ->
            return@mapNotNull masterOrderRepository.findAllByCompletedIsFalseAndSideAndCard(Side.SELL, userCard.card).minByOrNull { it.price }?.price?.let {
                userCard to it
            }
        }
        .sortedBy { it.second }
        .reversed()

        return list.subList(0, min(3, list.size))
    }
}
