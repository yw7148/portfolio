package com.youngwon.portfolio.contact.application

import com.youngwon.portfolio.contact.ContactSubmission
import com.youngwon.portfolio.contact.ContactUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class ContactCommandService(
    private val contactStore: ContactStore,
) : ContactUseCase {
    @Transactional
    override fun createContact(submission: ContactSubmission?): Boolean {
        if (submission == null) {
            return false
        }

        val contact = StoredContact(
            id = contactStore.nextId(),
            name = submission.name.orEmpty(),
            email = submission.email.orEmpty(),
            message = submission.message.orEmpty(),
            contactDate = LocalDateTime.now(ZoneId.of("Asia/Seoul")),
        )

        contactStore.save(contact)
        return true
    }
}
