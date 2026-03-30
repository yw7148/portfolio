package com.youngwon.portfolio.home.repository

import com.youngwon.portfolio.home.entity.ContactEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ContactRepository : JpaRepository<ContactEntity, Int> {
    @Query("SELECT MAX(contact.id) FROM ContactEntity contact")
    fun findMaxId(): Int?
}
