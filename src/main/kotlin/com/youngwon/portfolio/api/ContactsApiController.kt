package com.youngwon.portfolio.api

import com.youngwon.portfolio.contact.ContactSubmission
import com.youngwon.portfolio.contact.ContactUseCase
import com.youngwon.portfolio.generated.api.ContactsApi
import com.youngwon.portfolio.generated.model.ContactCreateRequest
import com.youngwon.portfolio.generated.model.ContactCreateResponse
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
