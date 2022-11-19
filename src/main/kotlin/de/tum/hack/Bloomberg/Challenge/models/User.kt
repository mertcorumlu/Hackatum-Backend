package de.tum.hack.Bloomberg.Challenge.models

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "users")
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
    var balance: Double,

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    var cards: List<UserCard> = emptyList()
)
