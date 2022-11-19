package de.tum.hack.Bloomberg.Challenge.models

import javax.persistence.*

@Entity
@Table(name = "SnapshotOrders")
open class SnapshotOrder {
    @Id
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "master_order_id", nullable = false)
    open var masterOrder: MasterOrder? = null

    @Column(name = "quantity", nullable = false)
    open var quantity: Int? = null
}
