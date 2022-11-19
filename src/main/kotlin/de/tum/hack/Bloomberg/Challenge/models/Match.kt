package de.tum.hack.Bloomberg.Challenge.models

import javax.persistence.*

@Entity
@Table(name = "Matches")
open class Match {
    @Id
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "buying_order_id", nullable = false)
    open var buyingOrder: ChildOrder? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "selling_order_id", nullable = false)
    open var sellingOrder: ChildOrder? = null
}
