package de.tum.hack.Bloomberg.Challenge.models

import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Entity

@Embeddable
open class UserCardId : Serializable {
    @Column(name = "user_id", nullable = false)
    open var userId: Int? = null

    @Column(name = "card_id", nullable = false)
    open var cardId: String? = null

    override fun hashCode(): Int = Objects.hash(userId, cardId)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        other as UserCardId

        return userId == other.userId &&
                cardId == other.cardId
    }

    companion object {
        private const val serialVersionUID = 4080125417379491223L
    }
}
