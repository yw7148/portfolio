package com.youngwon.portfolio.home.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "contact")
open class ContactEntity {
    @Id
    open var id: Int? = null

    open lateinit var name: String

    open lateinit var email: String

    open lateinit var message: String

    open lateinit var contactDate: LocalDateTime

    protected constructor()

    constructor(
        id: Int,
        name: String?,
        email: String?,
        message: String?,
        contactDate: LocalDateTime,
    ) : this() {
        this.id = id
        this.name = name.orEmpty()
        this.email = email.orEmpty()
        this.message = message.orEmpty()
        this.contactDate = contactDate
    }
}
