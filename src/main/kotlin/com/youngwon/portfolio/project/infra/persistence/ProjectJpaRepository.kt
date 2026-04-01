package com.youngwon.portfolio.project.infra.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface ProjectJpaRepository : JpaRepository<ProjectJpaEntity, Int> {
    fun findAllByOrderByIdAsc(): List<ProjectJpaEntity>
}
