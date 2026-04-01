package com.youngwon.portfolio.contact.infra.persistence

import com.youngwon.portfolio.contact.application.ContactStore
import com.youngwon.portfolio.contact.application.StoredContact
import org.springframework.stereotype.Repository

@Repository
class JpaContactStore(
    private val contactJpaRepository: ContactJpaRepository,
) : ContactStore {
    override fun nextId(): Int = (contactJpaRepository.findMaxId() ?: -1) + 1

    override fun save(contact: StoredContact) {
        contactJpaRepository.save(
            ContactJpaEntity(
                id = contact.id,
                name = contact.name,
                email = contact.email,
                message = contact.message,
                contactDate = contact.contactDate,
            ),
        )
    }
}
