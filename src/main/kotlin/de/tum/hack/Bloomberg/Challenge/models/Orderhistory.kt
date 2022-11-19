package de.tum.hack.Bloomberg.Challenge.models

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "orderhistory")
data class Orderhistory (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null,

    @Column(name = "quantity", nullable = false)
    var quantity: Int,

    @Column(name = "price", nullable = false)
    var price: Double,

    @Column(name = "side", nullable = false)
    var side: String,

    @Column(name = "created", nullable = false)
    var created: LocalDateTime,

    @ManyToOne
    var user: User,

    @ManyToOne
    var order: Order
)
