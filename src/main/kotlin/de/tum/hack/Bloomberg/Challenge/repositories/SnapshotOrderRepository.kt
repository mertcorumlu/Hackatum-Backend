package de.tum.hack.Bloomberg.Challenge.repositories;

import de.tum.hack.Bloomberg.Challenge.models.SnapshotOrder
import org.springframework.data.jpa.repository.JpaRepository

interface SnapshotOrderRepository : JpaRepository<SnapshotOrder, Int> {
}
