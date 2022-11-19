package de.tum.hack.Bloomberg.Challenge.models

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "users")
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null,

    @Column(name = "name")
    var name: String,

    @Column(name = "registered", nullable = false)
    var registered: LocalDateTime,

    @OneToMany(mappedBy = "user")
    var orderHistories: MutableSet<Orderhistory> = mutableSetOf(),

    @OneToMany(mappedBy = "user")
    var orders: MutableSet<Order> = mutableSetOf()
)
