package de.tum.hack.Bloomberg.Challenge.repositories;

import de.tum.hack.Bloomberg.Challenge.models.Card
import de.tum.hack.Bloomberg.Challenge.models.MasterOrder
import de.tum.hack.Bloomberg.Challenge.models.Side
import de.tum.hack.Bloomberg.Challenge.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface MasterOrderRepository : JpaRepository<MasterOrder, Int> {
    fun findAllByUserAndCardAndCompletedIsFalseAndSideAndPrice(user: User, card: Card, side: Side, price: Double): List<MasterOrder>
    fun findAllByUserAndCardAndCompletedIsFalseAndSide(user: User, card: Card, side: Side): List<MasterOrder>
    fun findAllByCompletedIsFalseAndSideAndCard(side: Side, card: Card): List<MasterOrder>
}
