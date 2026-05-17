package me.youngwon.portfolio.api

import me.youngwon.portfolio.contact.ContactSubmission
import me.youngwon.portfolio.contact.ContactUseCase
import me.youngwon.portfolio.generated.api.ContactsApi
import me.youngwon.portfolio.generated.model.ContactCreateRequest
import me.youngwon.portfolio.generated.model.ContactCreateResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ContactsApiController(
    private val contactUseCase: ContactUseCase,
) : ContactsApi {
    override fun createContact(contactCreateRequest: ContactCreateRequest): ResponseEntity<ContactCreateResponse> {
        val submission = ContactSubmission(
            name = contactCreateRequest.name,
            email = contactCreateRequest.email,
            message = contactCreateRequest.message,
        )

        val success = contactUseCase.createContact(submission)
        val status = if (success) HttpStatus.CREATED else HttpStatus.BAD_REQUEST

        return ResponseEntity.status(status)
            .body(ContactCreateResponse(success = success))
    }
}
