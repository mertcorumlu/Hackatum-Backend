package de.tum.hack.Bloomberg.Challenge.models

import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "Users")
open class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "name", nullable = false)
    open var name: String? = null

    @Column(name = "password", length = 50)
    open var password: String? = null

    @Column(name = "registered", nullable = false)
    open var registered: Instant? = null

    @Column(name = "balance", nullable = false)
    open var balance: Double? = null
}
