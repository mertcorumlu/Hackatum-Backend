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

    @ManyToOne
    @JoinColumn(name = "buying_order_id")
    var buyer: MasterOrder,

    @ManyToOne
    @JoinColumn(name = "selling_order_id")
    var seller: MasterOrder,

    @Column(name = "quantity", nullable = false)
    var quantity: Int,

    @Column(name = "price", nullable = false)
    var price: Double,

    @Column(name = "created", insertable = false)
    var created: LocalDateTime? = null
)
