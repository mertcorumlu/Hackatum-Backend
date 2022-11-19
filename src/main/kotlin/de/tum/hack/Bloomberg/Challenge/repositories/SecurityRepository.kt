package de.tum.hack.Bloomberg.Challenge.repositories;

import de.tum.hack.Bloomberg.Challenge.models.Security
import org.springframework.data.jpa.repository.JpaRepository

interface SecurityRepository : JpaRepository<Security, Int> {
}
