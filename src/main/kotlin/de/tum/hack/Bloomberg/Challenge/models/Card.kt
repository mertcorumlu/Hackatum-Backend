package de.tum.hack.Bloomberg.Challenge.models

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "Cards")
open class Card {
    @Id
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "card_id", nullable = false)
    open var cardId: String? = null

    @Column(name = "name", nullable = false)
    open var name: String? = null

    @Column(name = "rarity", length = 30)
    open var rarity: String? = null

    @Column(name = "image_url", nullable = false)
    open var imageUrl: String? = null
}
