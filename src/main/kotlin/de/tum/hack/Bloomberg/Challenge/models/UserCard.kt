package de.tum.hack.Bloomberg.Challenge.models

import javax.persistence.*

@Entity
@Table(name = "UserCards")
open class UserCard {
    @EmbeddedId
    open var id: UserCardId? = null

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    open var user: de.tum.hack.Bloomberg.Challenge.models.User? = null

    @MapsId("cardId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "card_id", nullable = false, referencedColumnName = "card_id")
    open var card: Card? = null

    @Column(name = "count", nullable = false)
    open var count: Int? = null
}
