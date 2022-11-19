package de.tum.hack.Bloomberg.Challenge.models

import javax.persistence.*

@Entity
@Table(name = "snapshot_orders")
data class SnapshotOrder (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null,

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "master_order_id", nullable = false)
    var masterOrder: MasterOrder,

    @Column(name = "quantity", nullable = false)
    var quantity: Int
)
