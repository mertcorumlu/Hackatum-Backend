package de.tum.hack.Bloomberg.Challenge.repositories;

import de.tum.hack.Bloomberg.Challenge.models.UserCard
import de.tum.hack.Bloomberg.Challenge.models.UserCardId
import org.springframework.data.jpa.repository.JpaRepository

interface UserCardRepository : JpaRepository<UserCard, UserCardId> {
}
