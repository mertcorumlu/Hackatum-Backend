package de.tum.hack.Bloomberg.Challenge.models

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "master_orders")
data class MasterOrder (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "card_id", nullable = false, referencedColumnName = "card_id", columnDefinition = "TEXT")
    var card: Card,

    @Column(name = "quantity", nullable = false)
    var quantity: Int,

    @Column(name = "price", nullable = false)
    var price: Double,

    @Column(name = "side", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    var side: Side,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Column(name = "created", insertable = false, updatable = false)
    var created: LocalDateTime? = null,

    @Column(name = "updated", insertable = false)
    var updated: LocalDateTime? = null,

    @Column(name = "completed")
    var completed: Boolean = false,

    @OneToOne(mappedBy = "masterOrder")
    @JsonIgnore
    var snapshotOrder: SnapshotOrder? = null
)
