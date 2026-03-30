package com.youngwon.portfolio.api

import com.youngwon.portfolio.generated.api.ContactsApi
import com.youngwon.portfolio.generated.model.ContactCreateRequest
import com.youngwon.portfolio.generated.model.ContactCreateResponse
import com.youngwon.portfolio.home.dto.Contact
import com.youngwon.portfolio.home.service.PortfolioService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ContactsApiController(
    private val portfolioService: PortfolioService,
) : ContactsApi {
    override fun createContact(contactCreateRequest: ContactCreateRequest): ResponseEntity<ContactCreateResponse> {
        val contact = Contact().apply {
            name = contactCreateRequest.name
            email = contactCreateRequest.email
            message = contactCreateRequest.message
        }

        val success = portfolioService.contactYoungwon(contact)
        val status = if (success) HttpStatus.CREATED else HttpStatus.BAD_REQUEST

        return ResponseEntity.status(status)
            .body(ContactCreateResponse(success = success))
    }
}
