package de.tum.hack.Bloomberg.Challenge.models

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "matches")
data class Match (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null,

    @Column(name = "price", nullable = false)
    var price: Double,

    @Column(name = "quantity", nullable = false)
    var quantity: Int,

    @Column(name = "date_buyer")
    var dateBuyer: LocalDateTime,

    @Column(name = "date_seller")
    var dateSeller: LocalDateTime,

    @Column(name = "deleted")
    var deleted: Boolean,

    @ManyToOne
    var buyingOrder: Order,

    @ManyToOne
    var sellinOrder: Order,
)
