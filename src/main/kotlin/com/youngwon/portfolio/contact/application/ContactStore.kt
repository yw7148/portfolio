package com.youngwon.portfolio.contact.application

import java.time.LocalDateTime

interface ContactStore {
    fun nextId(): Int

    fun save(contact: StoredContact)
}

data class StoredContact(
    val id: Int,
    val name: String,
    val email: String,
    val message: String,
    val contactDate: LocalDateTime,
)
