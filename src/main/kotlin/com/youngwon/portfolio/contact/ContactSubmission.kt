package com.youngwon.portfolio.contact

data class ContactSubmission @JvmOverloads constructor(
    val name: String? = null,
    val email: String? = null,
    val message: String? = null,
)
