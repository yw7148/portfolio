package com.youngwon.portfolio.contact

interface ContactUseCase {
    fun createContact(submission: ContactSubmission?): Boolean
}
