package de.tum.hack.Bloomberg.Challenge.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "user_cards")
data class UserCard (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null,

    @ManyToOne
    var user: User,

    @ManyToOne
    @JoinColumn(name = "card_id", referencedColumnName = "card_id")
    var card: Card,

    @Column(name = "count", nullable = false)
    var count: Int
)

