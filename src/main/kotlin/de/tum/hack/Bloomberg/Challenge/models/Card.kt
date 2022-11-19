package de.tum.hack.Bloomberg.Challenge.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "cards")
data class Card (
    @Id
    @Column(name = "id", nullable = false)
    var id: Int? = null,

    @Column(name = "card_id", nullable = false)
    var cardId: String,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "rarity", length = 30)
    var rarity: String? = null,

    @Column(name = "image_url", nullable = false)
    var imageUrl: String,

    @JsonIgnore
    @OneToMany(mappedBy = "card")
    var masterOrders: List<MasterOrder> = emptyList()
)
