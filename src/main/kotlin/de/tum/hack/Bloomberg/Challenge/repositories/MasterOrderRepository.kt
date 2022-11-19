package de.tum.hack.Bloomberg.Challenge.repositories;

import de.tum.hack.Bloomberg.Challenge.models.MasterOrder
import org.springframework.data.jpa.repository.JpaRepository

interface MasterOrderRepository : JpaRepository<MasterOrder, Int> {
}
