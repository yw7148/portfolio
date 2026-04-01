package com.youngwon.portfolio.contact.infra.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ContactJpaRepository : JpaRepository<ContactJpaEntity, Int> {
    @Query("select max(contact.id) from ContactJpaEntity contact")
    fun findMaxId(): Int?
}
