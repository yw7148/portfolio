package com.youngwon.portfolio.home.dto

data class Contact @JvmOverloads constructor(
    var name: String? = null,
    var email: String? = null,
    var message: String? = null,
)
