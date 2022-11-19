package de.tum.hack.Bloomberg.Challenge.models

import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "MasterOrders")
open class MasterOrder {
    @Id
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "card_id", nullable = false, referencedColumnName = "card_id")
    open var card: Card? = null

    @Column(name = "quantity", nullable = false)
    open var quantity: Int? = null

    @Column(name = "price", nullable = false)
    open var price: Double? = null

    @Column(name = "side", nullable = false)
    open var side: Boolean? = false

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    open var user: de.tum.hack.Bloomberg.Challenge.models.User? = null

    @Column(name = "created", nullable = false)
    open var created: Instant? = null

    @Column(name = "updated", nullable = false)
    open var updated: Instant? = null

    @Column(name = "completed")
    open var completed: Boolean? = null
}
