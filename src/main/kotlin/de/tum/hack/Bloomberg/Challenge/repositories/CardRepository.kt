package de.tum.hack.Bloomberg.Challenge.repositories;

import de.tum.hack.Bloomberg.Challenge.models.Card
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CardRepository : JpaRepository<Card, Int> {
    fun findByCardId(card_id: String): Card?

    fun findTop5ByCardIdInListAndOrderByNameAsc(list: List<String>): List<Card>

    fun findAllByCardIdInListAndOrderByRarity(list: List<String>): List<Card>
}
