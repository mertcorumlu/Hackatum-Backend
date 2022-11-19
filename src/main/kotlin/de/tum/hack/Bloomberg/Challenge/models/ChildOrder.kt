package de.tum.hack.Bloomberg.Challenge.models

import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "ChildOrders")
open class ChildOrder {
    @Id
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    open var order: de.tum.hack.Bloomberg.Challenge.models.MasterOrder? = null

    @Column(name = "quantity", nullable = false)
    open var quantity: Int? = null

    @Column(name = "price", nullable = false)
    open var price: Double? = null

    @Column(name = "created")
    open var created: Instant? = null
}
