package com.youngwon.portfolio.project.infra.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "project_program")
open class ProjectProgramJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int = 0

    @Column(name = "program_id", nullable = false)
    open var programId: Int = 0

    @Column(name = "project_id", nullable = false)
    open var projectId: Int = 0

    @Column(name = "link", nullable = false)
    open lateinit var link: String

    protected constructor()
}
