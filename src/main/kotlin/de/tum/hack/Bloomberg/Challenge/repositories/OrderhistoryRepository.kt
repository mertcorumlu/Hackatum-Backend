package de.tum.hack.Bloomberg.Challenge.repositories;

import de.tum.hack.Bloomberg.Challenge.models.Orderhistory
import org.springframework.data.jpa.repository.JpaRepository

interface OrderhistoryRepository : JpaRepository<Orderhistory, Int> {
}
