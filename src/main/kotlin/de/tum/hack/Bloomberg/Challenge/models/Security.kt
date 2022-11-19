package de.tum.hack.Bloomberg.Challenge.models

import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "security")
data class Security (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null,

    @Column(name = "name", nullable = false, length = 50)
    var name: String? = null,

    @Column(name = "registered", nullable = false)
    var registered: Instant? = null
)
