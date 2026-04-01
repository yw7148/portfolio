package com.youngwon.portfolio.contact.infra.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "contact")
open class ContactJpaEntity {
    @Id
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "name", nullable = false)
    open lateinit var name: String

    @Column(name = "email", nullable = false)
    open lateinit var email: String

    @Column(name = "message", nullable = false)
    open lateinit var message: String

    @Column(name = "contact_date", nullable = false)
    open lateinit var contactDate: LocalDateTime

    protected constructor()

    constructor(
        id: Int,
        name: String,
        email: String,
        message: String,
        contactDate: LocalDateTime,
    ) : this() {
        this.id = id
        this.name = name
        this.email = email
        this.message = message
        this.contactDate = contactDate
    }
}
