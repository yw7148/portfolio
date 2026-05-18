package me.youngwon.portfolio.contact.application

import me.youngwon.portfolio.contact.ContactSubmission
import me.youngwon.portfolio.contact.ContactUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class ContactCommandService(
    private val contactStore: ContactStore,
) : ContactUseCase {
    @Transactional
    override fun createContact(submission: ContactSubmission): Boolean {
        val contact = StoredContact(
            id = contactStore.nextId(),
            name = submission.name,
            email = submission.email,
            message = submission.message,
            contactDate = LocalDateTime.now(ZoneId.of("Asia/Seoul")),
        )

        contactStore.save(contact)
        return true
    }
}
