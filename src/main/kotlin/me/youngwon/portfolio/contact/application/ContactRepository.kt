package me.youngwon.portfolio.contact.application

import java.time.LocalDateTime

interface ContactRepository {
    fun save(contact: StoredContact)
}

data class StoredContact(
    val name: String,
    val email: String,
    val message: String,
    val contactDate: LocalDateTime,
)
