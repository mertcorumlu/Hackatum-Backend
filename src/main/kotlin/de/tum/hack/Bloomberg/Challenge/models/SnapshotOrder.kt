package de.tum.hack.Bloomberg.Challenge.models

import javax.persistence.*

@Entity
@Table(name = "SnapshotOrders")
data class SnapshotOrder (
    @Id
    @Column(name = "id", nullable = false)
    var id: Int? = null,

    @Column(name = "card_id", unique = true, nullable = false)
    var card_id: String,

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "master_order_id", nullable = false)
    var masterOrder: MasterOrder,

    @Column(name = "quantity", nullable = false)
    var quantity: Int
)
