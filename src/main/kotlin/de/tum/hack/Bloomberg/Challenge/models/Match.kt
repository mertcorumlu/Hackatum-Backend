package de.tum.hack.Bloomberg.Challenge.models

import javax.persistence.*

@Entity
@Table(name = "matches")
data class Match (
    @Id
    @Column(name = "id", nullable = false)
    var id: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "buying_order_id", nullable = false)
    var buyingOrder: ChildOrder,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "selling_order_id", nullable = false)
    var sellingOrder: ChildOrder
)
