package de.tum.hack.Bloomberg.Challenge.models

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "Users")
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "password", length = 50)
    var password: String,

    @Column(name = "registered", nullable = false)
    var registered: LocalDateTime,

    @Column(name = "balance", nullable = false)
    var balance: Double
)
