package de.tum.hack.Bloomberg.Challenge.models

import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "orders")
data class Order (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null,

    @Column(name = "quantity", nullable = false)
    var quantity: Int? = null,

    @Column(name = "price", nullable = false)
    var price: Double? = null,

    @Column(name = "side", nullable = false)
    var side: String? = null,

    @Column(name = "created", nullable = false)
    var created: Instant? = null,

    @Column(name = "updated", nullable = false)
    var updated: Instant? = null,

    @OneToMany(mappedBy = "order")
    var orderhistories: MutableSet<Orderhistory> = mutableSetOf(),

    @ManyToOne
    var user: User
)
