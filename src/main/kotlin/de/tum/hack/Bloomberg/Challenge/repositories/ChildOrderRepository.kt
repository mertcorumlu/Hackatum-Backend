package de.tum.hack.Bloomberg.Challenge.repositories;

import de.tum.hack.Bloomberg.Challenge.models.ChildOrder
import org.springframework.data.jpa.repository.JpaRepository

interface ChildOrderRepository : JpaRepository<ChildOrder, Int> {
}
