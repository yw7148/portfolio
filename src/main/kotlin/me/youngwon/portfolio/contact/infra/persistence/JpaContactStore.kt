package me.youngwon.portfolio.contact.infra.persistence

import me.youngwon.portfolio.contact.application.ContactStore
import me.youngwon.portfolio.contact.application.StoredContact
import org.springframework.stereotype.Repository

@Repository
class JpaContactStore(
    private val contactJpaRepository: ContactJpaRepository,
) : ContactStore {
    override fun save(contact: StoredContact) {
        contactJpaRepository.save(
            ContactJpaEntity(
                name = contact.name,
                email = contact.email,
                message = contact.message,
                contactDate = contact.contactDate,
            ),
        )
    }
}
