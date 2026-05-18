package me.youngwon.portfolio.contact.infra.persistence

import org.springframework.data.jpa.repository.JpaRepository
interface ContactJpaRepository : JpaRepository<ContactJpaEntity, Int>
