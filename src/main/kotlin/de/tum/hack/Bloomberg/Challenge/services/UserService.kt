package de.tum.hack.Bloomberg.Challenge.services

import de.tum.hack.Bloomberg.Challenge.api.Count
import de.tum.hack.Bloomberg.Challenge.api.SortTypes
import de.tum.hack.Bloomberg.Challenge.models.Side
import de.tum.hack.Bloomberg.Challenge.models.User
import de.tum.hack.Bloomberg.Challenge.models.UserCard
import de.tum.hack.Bloomberg.Challenge.repositories.CardRepository
import de.tum.hack.Bloomberg.Challenge.repositories.MasterOrderRepository
import de.tum.hack.Bloomberg.Challenge.repositories.UserCardRepository
import de.tum.hack.Bloomberg.Challenge.repositories.UserRepository
import org.springframework.data.repository.findByIdOrNull
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

    fun getUser(username: String): User {
        return userRepository.findByName(username)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No user found")
    }

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

    fun getUserCardCount(userId: Int, cardId: String): Count {
        val user = userRepository.findByIdOrNull(userId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val card = cardRepository.findByCardId(cardId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val userCardCount = userCardRepository.findFirstByUserAndCard(user, card)?.count ?: 0
        val sellerCount = masterOrderRepository.findAllByUserAndCardAndCompletedIsFalseAndSide(user, card, Side.SELL).mapNotNull { it.snapshotOrder?.quantity }.sum()
        return Count(count = if (userCardCount > 0) userCardCount - sellerCount else 0)
    }

    fun getTop(userId: Int): List<Pair<UserCard, Double>> {
        val userCards = userCardRepository.findAllByUserId(userId)

        return userCards.map { userCard ->
            userCard.count = getUserCardCount(userId, userCard.card.cardId).count
            return@map masterOrderRepository.findAllByCompletedIsFalseAndSideAndCard(Side.SELL, userCard.card)
                .minByOrNull { it.price }?.price?.let {
                userCard to it
            } ?: (userCard to 0.0)
        }
        .sortedBy { it.second }
        .reversed()
    }

    fun getTop3(userId: Int): List<Pair<UserCard, Double>>  {
        val list = getTop(userId)
        return list.subList(0, min(3, list.size))
    }
}
