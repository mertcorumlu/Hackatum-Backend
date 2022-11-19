package de.tum.hack.Bloomberg.Challenge.models

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "UserCards")
@IdClass(UserCardId::class)
data class UserCard (

    @Id
    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Id
    @MapsId("cardId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "card_id", nullable = false, referencedColumnName = "card_id")
    var card: Card,

    @Column(name = "count", nullable = false)
    var count: Int
)

class UserCardId (
    val user: Int,
    val card: Int
): Serializable
